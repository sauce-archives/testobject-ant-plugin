package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class RunCheckupTask extends AbstractTask {

	@Override
	public void execute() throws BuildException {
		client.runCheckup(getUserProperty(), getAppProperty());

		log("checkup started", Project.MSG_INFO);
	}
}
