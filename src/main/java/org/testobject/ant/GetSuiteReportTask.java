package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.testobject.ant.client.BatchReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// duplicate of deprecated GetBatchReportTask (tk)
public class GetSuiteReportTask extends AbstractTask {

	private long suiteReportId;
	private String status = Constants.PROPERTY_TEST_SUITE_STATUS;
	private String tests = Constants.PROPERTY_TEST_SUITE_TESTS;
	private String errors = Constants.PROPERTY_TEST_SUITE_ERRORS;

	//	private File out;

	@Override
	public void execute() throws BuildException {
		BatchReport suiteReport = client.getBatchReport(getUserProperty(), getAppProperty(), suiteReportId);

		Map<String, String> devices = getDevices(suiteReport);
		Map<Long, TestSuites.TestSuite> tests = getTests(suiteReport);

		processReportToTestCases(suiteReport, devices, tests);

		TestSuites testSuites = new TestSuites(suiteReport.getName());
		for (TestSuites.TestSuite testSuite : tests.values()) {
			testSuites.tests += testSuite.tests;
			testSuites.errors += testSuite.errors;
			testSuites.testSuites.add(testSuite);
		}

		testSuites.time = suiteReport.getDuration();

		log(String.format("test suite status: %s tests: %d errors: %d", suiteReport.getStatus(), testSuites.tests, testSuites.errors),
				Project.MSG_INFO);

		getProject().setProperty(this.status, suiteReport.getStatus().toString());
		getProject().setProperty(this.tests, Integer.toString(testSuites.tests));
		getProject().setProperty(this.errors, Integer.toString(testSuites.errors));

		//		saveReportToFile(testSuites);

		//		log(String.format("report saved in file: %s", out.getAbsolutePath()), Project.MSG_INFO);

	}

	//	private void saveReportToFile(TestSuites testSuites) {
	//		try (BufferedWriter output = new BufferedWriter(new FileWriter(out))) {
	//			output.write(testSuites.toString());
	//		} catch (IOException e) {
	//			throw new BuildException(e);
	//		}
	//	}

	private void processReportToTestCases(BatchReport suiteReport, Map<String, String> devices, Map<Long, TestSuites.TestSuite> tests) {
		List<BatchReport.ReportEntry> reports = suiteReport.getReports();
		for (BatchReport.ReportEntry reportEntry : reports) {
			TestSuites.TestSuite.TestCase testCase = toTestCase(devices, reportEntry);

			TestSuites.TestSuite testSuite = tests.get(reportEntry.getKey().getTestId());
			testSuite.tests += 1;
			testSuite.time += testCase.time;
			switch (reportEntry.getView().getStatus()) {
			case SUCCESS:
			case WARNING:
				break;
			default:
				testSuite.errors += 1;
			}
			testSuite.testCases.add(testCase);
		}
	}

	private Map<Long, TestSuites.TestSuite> getTests(BatchReport suiteReport) {
		Map<Long, TestSuites.TestSuite> tests = new HashMap<Long, TestSuites.TestSuite>();
		{
			List<BatchReport.TestView> testViews = suiteReport.getTests();
			for (BatchReport.TestView testView : testViews) {
				tests.put(testView.getTestId(), new TestSuites.TestSuite(testView.getName()));
			}
		}
		return tests;
	}

	private Map<String, String> getDevices(BatchReport suiteReport) {
		Map<String, String> devices = new HashMap<String, String>();
		{
			List<BatchReport.DeviceView> deviceViews = suiteReport.getDevices();
			for (BatchReport.DeviceView deviceView : deviceViews) {
				devices.put(deviceView.getDeviceId(), deviceView.getName());
			}
		}
		return devices;
	}

	private TestSuites.TestSuite.TestCase toTestCase(Map<String, String> devices, BatchReport.ReportEntry reportEntry) {
		BatchReport.ReportKey reportKey = reportEntry.getKey();
		BatchReport.Status status = reportEntry.getView().getStatus();
		String deviceName = devices.get(reportKey.getDeviceId());

		return new TestSuites.TestSuite.TestCase(deviceName, status.toString());
	}

	public void setSuiteReportId(long suiteReportId) {
		this.suiteReportId = suiteReportId;
	}

	//	public void setOut(File out) {
	//		this.out = out;
	//	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTests(String tests) {
		this.tests = tests;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

}
