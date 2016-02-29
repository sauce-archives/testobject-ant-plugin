package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.testobject.ant.client.BatchReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class GetBatchReportTask extends AbstractTask {

	private long batchReportId;
	private String status = Constants.PROPERTY_BATCH_STATUS;
	private String tests = Constants.PROPERTY_BATCH_TESTS;
	private String errors = Constants.PROPERTY_BATCH_ERRORS;

	//	private File out;

	@Override
	public void execute() throws BuildException {
//		log("the 'downloadBatchReport' task is deprecated. Please use the task 'downloadSuiteReport' instead.", Project.MSG_WARN);
//
//		BatchReport batchReport = client.getBatchReport(getUserProperty(), getAppProperty(), batchReportId);
//
//		Map<String, String> devices = getDevices(batchReport);
//		Map<Long, TestSuites.TestSuite> tests = getTests(batchReport);
//
//		processReportToTestCases(batchReport, devices, tests);
//
//		TestSuites testSuites = new TestSuites(batchReport.getName());
//		for (TestSuites.TestSuite testSuite : tests.values()) {
//			testSuites.tests += testSuite.tests;
//			testSuites.errors += testSuite.errors;
//			testSuites.testSuites.add(testSuite);
//		}
//
//		testSuites.time = batchReport.getDuration();
//
//		log(String.format("batch status: %s tests: %d errors: %d", batchReport.getStatus(), testSuites.tests, testSuites.errors),
//				Project.MSG_INFO);
//
//		getProject().setProperty(this.status, batchReport.getStatus().toString());
//		getProject().setProperty(this.tests, Integer.toString(testSuites.tests));
//		getProject().setProperty(this.errors, Integer.toString(testSuites.errors));

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

	private void processReportToTestCases(BatchReport batchReport, Map<String, String> devices, Map<Long, TestSuites.TestSuite> tests) {
		List<BatchReport.ReportEntry> reports = batchReport.getReports();
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

	private Map<Long, TestSuites.TestSuite> getTests(BatchReport batchReport) {
		Map<Long, TestSuites.TestSuite> tests = new HashMap<Long, TestSuites.TestSuite>();
		{
			List<BatchReport.TestView> testViews = batchReport.getTests();
			for (BatchReport.TestView testView : testViews) {
				tests.put(testView.getTestId(), new TestSuites.TestSuite(testView.getName()));
			}
		}
		return tests;
	}

	private Map<String, String> getDevices(BatchReport batchReport) {
		Map<String, String> devices = new HashMap<String, String>();
		{
			List<BatchReport.DeviceView> deviceViews = batchReport.getDevices();
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

	public void setBatchReportId(long batchReportId) {
		this.batchReportId = batchReportId;
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
