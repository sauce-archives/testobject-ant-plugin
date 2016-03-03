package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;

public class LoginTask extends AbstractTask {

	private String username;
	private String password;

	@Override
	public void execute() throws BuildException {
		try {
			log(String.format("trying to log in with user %s and password %s", username, password), Project.MSG_INFO);
			client.login(username, password);

			PropertyHelper.setNewProperty(getProject(), "org.testobject.client", client);

			log(String.format("user %s successfully logged in", username), Project.MSG_INFO);
		} catch (Exception e) {
			log(String.format("Exception: %s", e.getMessage()), Project.MSG_ERR);
			throw new BuildException(String.format("unable to login user %s into endpoint %s", username, endpoint), e);
		}

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
