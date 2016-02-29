package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class ActivateVersionTask extends AbstractTask {

	private long versionId;

	@Override
	public void execute() throws BuildException {
//		client.activateVersion(getUserProperty(), getAppProperty(), versionId);
//
//		log(String.format("version %d activated", versionId), Project.MSG_INFO);
	}

	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}
}
