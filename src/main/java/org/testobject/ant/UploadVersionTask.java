package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.testobject.api.TestObjectClient;
import org.testobject.rest.api.TestSuiteReport;
import org.testobject.rest.api.TestSuiteResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class UploadVersionTask extends AbstractTask {

	private String name;
	private File file;
	private String response = Constants.PROPERTY_VERSION_NEW;

	@Override
	public void execute() throws BuildException {

		String username = getUserProperty();
		String app = getAppProperty();
		String appApk = getAppApkProperty();
		String testApk = getTestApkProperty();
		Long testSuite = Long.parseLong(getProject().getProperty("testobject.testSuite.id")); // TODO
		String team = getProject().getProperty("testobject.team") != null && !getProject().getProperty("testobject.team").isEmpty() ? getProject().getProperty("testobject.team") : username;
		Boolean runAsPackage = getProject().getProperty("testobject.runaspackage") != null ? Boolean.parseBoolean(getProject().getProperty("testobject.runaspackage")) : false;

		log("username = " + username);
		log("app = " + app);
		log("testsuite = " + testSuite);
		log("team = " + team);
		log("runAsPackage = " + runAsPackage);

		// TODO load files
		File appApkFile = new File("app.apk");
		File testApkFile = new File("test.apk");

		TestObjectClient client = (TestObjectClient) PropertyHelper.getProperty(getProject(), "org.testobject.client");

		TestSuiteResource.InstrumentationTestSuiteRequest instrumentationTestSuiteRequest = new TestSuiteResource.InstrumentationTestSuiteRequest(runAsPackage);

		try {
			client.updateInstrumentationTestSuite(team, app, testSuite, appApkFile, testApkFile, instrumentationTestSuiteRequest);
			log(String.format("Uploaded appAPK : %s and testAPK : %s", appApkFile.getAbsolutePath(), testApkFile.getAbsolutePath()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(String.format("unable to update testSuite %s ", testSuite));
		}

	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	// TODO move
	private void writeSuiteReportXML(TestObjectClient client, String user, String app, long suiteReportId) {

		String filename = user + "-" + app + "-" + suiteReportId + ".xml";
		String xml = client.readTestSuiteXMLReport(user, app, suiteReportId);
		File file = new File(Paths.get(".", "testobject").toAbsolutePath().toUri()); // TODO check directory
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

}