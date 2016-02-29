package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

@Deprecated
public class StartBatchTask extends AbstractTask {

	private long batchId;
	private String response = Constants.PROPERTY_BATCH_REPORT_NEW;

	@Override
	public void execute() throws BuildException {
//		log("the 'startBatch' task is deprecated. Please use the task 'startTestSuite' instead.", Project.MSG_WARN);
//
//		long batchReportId = client.startBatch(getUserProperty(), getAppProperty(), batchId);
//		getProject().setProperty(response, Long.toString(batchReportId));
//
//		log(String.format("batch %d started, reportid is %d", batchId, batchReportId), Project.MSG_INFO);

	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
