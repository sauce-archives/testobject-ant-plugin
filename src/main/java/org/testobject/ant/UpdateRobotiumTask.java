package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UpdateRobotiumTask extends AbstractTask {

	private long robotiumSuiteId;
	private File file;

	@Override
	public void execute() throws BuildException {
//		try {
//			String uploadId = client.uploadFile(getUserProperty(), getAppProperty(), file.getName(), new FileInputStream(file));
//			client.updateRobotiumBatch(getUserProperty(), getAppProperty(), robotiumSuiteId, uploadId);
//
//			log(String.format("robotium suite with id %d updated", robotiumSuiteId), Project.MSG_INFO);
//		} catch (FileNotFoundException e) {
//			String cause = String.format("unable to find file '%s' to update robotium suite", file.getAbsolutePath());
//			throw new BuildException(cause, e);
//		}
	}

	public void setRobotiumSuiteid(long robotiumSuiteId) {
		this.robotiumSuiteId = robotiumSuiteId;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
