package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.testobject.api.TestObjectClient;
import org.testobject.rest.api.TestSuiteReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GetSuiteReportTask extends AbstractTask {

	private long suiteReportId;
	private String status = Constants.PROPERTY_TEST_SUITE_STATUS;
	private String tests = Constants.PROPERTY_TEST_SUITE_TESTS;
	private String errors = Constants.PROPERTY_TEST_SUITE_ERRORS;

	@Override
	public void execute() throws BuildException {

		TestObjectClient client = (TestObjectClient) PropertyHelper.getProperty(getProject(), "org.testobject.client");

		long start = System.currentTimeMillis();

		TestSuiteReport suiteReport = client.waitForSuiteReport(getUserProperty(), getAppProperty(), suiteReportId);

		writeSuiteReportXML(client, getTeamProperty(), getAppProperty(), suiteReportId);

		long end = System.currentTimeMillis();

		String executionTime = getExecutionTime(start, end);

		logResults(suiteReport, executionTime);

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

	}

	public void logResults(TestSuiteReport suiteReport, String executionTime) {

		int errors = countErrors(suiteReport);

		String reportURL = String
				.format("%s/#/%s/%s/robotium/%d/reports/%d", baseUrl.replace("/api/rest", ""), getTeamProperty(), getAppProperty(), Long.parseLong(getTestSuiteProperty()), suiteReportId);

		StringBuilder msg = new StringBuilder();

		msg.append("\n");
		msg.append("--- Test suite " + getTestSuiteProperty() + " terminated run #" +  suiteReportId + ". ---");
		msg.append("\n");
		msg.append(getTestsList(suiteReport));
		msg.append("----------------------------------------------------------------------------------");
		msg.append("\n");
		msg.append(String.format("Ran %d tests in %s", suiteReport
				.getReports().size(), executionTime));
		msg.append("\n");
		msg.append("Suite status: " + suiteReport.getStatus());
		msg.append("\n");

		if (errors > 0) {
			msg.append(String.format("List of failed Test (Total errors : %d)", errors));
			msg.append("\n");
			msg.append(failedTestsList(suiteReport, reportURL));
			msg.append("\n");
		}

		msg.append(String.format("Report URL : '%s'", reportURL));

		log(msg.toString());

	}

	private void processReportToTestCases(TestSuiteReport suiteReport, Map<String, String> devices, Map<Long, TestSuites.TestSuite> tests) {
		List<TestSuiteReport.ReportEntry> reports = suiteReport.getReports();
		for (TestSuiteReport.ReportEntry reportEntry : reports) {
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

	private Map<Long, TestSuites.TestSuite> getTests(TestSuiteReport suiteReport) {
		Map<Long, TestSuites.TestSuite> tests = new HashMap<Long, TestSuites.TestSuite>();
		{
			List<TestSuiteReport.TestView> testViews = suiteReport.getTests();
			for (TestSuiteReport.TestView testView : testViews) {
				tests.put(testView.getTestId(), new TestSuites.TestSuite(testView.getName()));
			}
		}
		return tests;
	}

	private Map<String, String> getDevices(TestSuiteReport suiteReport) {
		Map<String, String> devices = new HashMap<String, String>();
		{
			List<TestSuiteReport.DeviceView> deviceViews = suiteReport.getDevices();
			for (TestSuiteReport.DeviceView deviceView : deviceViews) {
				devices.put(deviceView.getDeviceId(), deviceView.getName());
			}
		}
		return devices;
	}

	private TestSuites.TestSuite.TestCase toTestCase(Map<String, String> devices, TestSuiteReport.ReportEntry reportEntry) {
		TestSuiteReport.ReportKey reportKey = reportEntry.getKey();
		TestSuiteReport.Status status = reportEntry.getView().getStatus();
		String deviceName = devices.get(reportKey.getDeviceId());

		return new TestSuites.TestSuite.TestCase(deviceName, status.toString());
	}

	private void writeSuiteReportXML(TestObjectClient client, String user, String app, long suiteReportId) {

		String filename = user + "-" + app + "-" + suiteReportId + ".xml";
		String xml = client.readTestSuiteXMLReport(user, app, suiteReportId);
		File file = new File(Paths.get(".", "reports").toAbsolutePath().toUri());
		if (!file.isDirectory()) {
			file.mkdir();
		}
		try {
			Files.write(Paths.get(".", "testobject", filename), xml.getBytes());
			log("Wrote XML report to '" + filename + "'");
		} catch (IOException e) {
			log("Failed to save XML report: " + e.getMessage());
		}
	}

	// TODO move
	/**
	 * get Formatted Execution Time for printing
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	private static String getExecutionTime(final long start, final long end) {
		NumberFormat formatter = new DecimalFormat("#.000");
		long millis = end - start;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		String seconds = formatter.format(millis / 1000d);
		String executionTime;
		if (minutes > 0) {
			executionTime = String.format("%dm (%ss)", minutes, seconds);
		} else {
			executionTime = String.format("%ss", seconds);
		}
		return executionTime;
	}

	private static int countErrors(TestSuiteReport suiteReport) {
		int errors = 0;
		Iterator<TestSuiteReport.ReportEntry> reportsIterator = suiteReport.getReports().iterator();
		while (reportsIterator.hasNext()) {
			TestSuiteReport.ReportEntry reportEntry = reportsIterator.next();
			if (reportEntry.getView().getStatus() == TestSuiteReport.Status.FAILURE) {
				errors++;
			}
		}
		return errors;
	}

	private static String getTestsList(TestSuiteReport suiteReport) {
		StringBuilder list = new StringBuilder();
		Iterator<TestSuiteReport.ReportEntry> reportsIterator = suiteReport.getReports().iterator();
		while (reportsIterator.hasNext()) {
			TestSuiteReport.ReportEntry reportEntry = reportsIterator.next();
			String testName = getTestName(suiteReport, reportEntry.getKey().getTestId());
			String deviceId = reportEntry.getKey().getDeviceId();
			list.append(String.format("%s - %s .............  %s", testName, deviceId, reportEntry.getView().getStatus().toString()));
			list.append("\n");
		}
		return list.toString();
	}

	private static String failedTestsList(TestSuiteReport suiteReport, String baseReportUrl) {
		StringBuilder list = new StringBuilder();
		Iterator<TestSuiteReport.ReportEntry> reportsIterator = suiteReport.getReports().iterator();
		while (reportsIterator.hasNext()) {
			TestSuiteReport.ReportEntry reportEntry = reportsIterator.next();
			if (reportEntry.getView().getStatus() == TestSuiteReport.Status.FAILURE) {
				String testName = getTestName(suiteReport, reportEntry.getKey().getTestId());
				String deviceId = reportEntry.getKey().getDeviceId();
				String url = String.format("%s/executions/%d",
						baseReportUrl, reportEntry.getView().getReportId());
				list.append(String.format("%s - %s ....  %s", testName, deviceId, url));
				list.append("\n");
			}
		}
		return list.toString();
	}

	private static String getTestName(TestSuiteReport suiteReport, long testId) {
		Iterator<TestSuiteReport.TestView> testViewIterator = suiteReport.getTests().iterator();
		while (testViewIterator.hasNext()) {
			TestSuiteReport.TestView testView = testViewIterator.next();
			if (testView.getTestId() == testId) {
				return testView.getName();
			}
		}
		return "";
	}


	public void setSuiteReportId(long suiteReportId) {
		this.suiteReportId = suiteReportId;
	}

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
