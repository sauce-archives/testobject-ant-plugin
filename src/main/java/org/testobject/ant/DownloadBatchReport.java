package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.testobject.ant.client.BatchReport;

public class DownloadBatchReport extends AbstractTask {

	private long batchId;

	@Override
	public void execute() throws BuildException {
//		BatchReport batchReport = client.getBatchReport(getUserProperty(), getAppProperty(), batchId);
//
//		log("batch result: " + batchReport.getStatus());
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

}
