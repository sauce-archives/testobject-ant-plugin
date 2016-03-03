package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.testobject.api.TestObjectClient;

// duplicate of deprecated StartBatchTask (tk)
public class StartTestSuiteTask extends AbstractTask {

	private long testSuiteId;
	private String response = Constants.PROPERTY_SUITE_REPORT_NEW;

	@Override
	public void execute() throws BuildException {

		TestObjectClient client = (TestObjectClient) PropertyHelper.getProperty(getProject(), "org.testobject.client");

		long suiteReportId = client.startInstrumentationTestSuite(getUserProperty(), getAppProperty(), testSuiteId); // TODO method changed, check if it works
		getProject().setProperty(response, Long.toString(suiteReportId));

		log(String.format("test suite %d started, suiteReport id is %d", testSuiteId, suiteReportId), Project.MSG_INFO);
		getProject().setProperty("testobject.suite.report.id", Long.toString(suiteReportId));

	}

	public void setTestSuiteId(long testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
