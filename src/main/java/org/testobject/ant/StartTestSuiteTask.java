package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

// duplicate of deprecated StartBatchTask (tk)
public class StartTestSuiteTask extends AbstractTask {

	private long testSuiteId;
	private String response = Constants.PROPERTY_SUITE_REPORT_NEW;

	@Override
	public void execute() throws BuildException {
		long suiteReportId = client.startBatch(getUserProperty(), getAppProperty(), testSuiteId);
		getProject().setProperty(response, Long.toString(suiteReportId));

		log(String.format("test suite %d started, suiteReport id is %d", testSuiteId, suiteReportId), Project.MSG_INFO);

	}

	public void setTestSuiteId(long testSuiteId) {
		this.testSuiteId = testSuiteId;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
