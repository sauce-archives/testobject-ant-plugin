package org.testobject.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.testobject.api.TestObjectClient;

public abstract class AbstractTask extends Task {

	protected final String baseUrl = "https://app.testobject.com/api/rest";

	protected boolean ssl = true;
	protected String endpoint = "app.testobject.com";
	protected TestObjectClient client;

	public AbstractTask() {}

	@Override
	public void init() throws BuildException {
		initSSL();
		initEndpoint();
		initTestObjectClient();

		authenticate();

	}

	private void initSSL() {
		String ssl = getProject().getProperty(Constants.PROPERTY_SSL);
		if (isNotNullNorEmpty(ssl)) {
			this.ssl = Boolean.valueOf(ssl);
		}
	}

	private void initEndpoint() {
		String endpoint = getProject().getProperty(Constants.PROPERTY_ENDPOINT);
		if (isNotNullNorEmpty(endpoint)) {
			this.endpoint = endpoint;
		}
	}

	private void initTestObjectClient() throws BuildException {
		try {
			this.client = TestObjectClient.Factory.create(baseUrl); // TODO proxy settings missing
		} catch (Exception e) {
			throw new BuildException(String.format("unable to initialize testobject client for endpoint '%s'", endpoint), e);
		}
	}

	protected void authenticate() { // TODO obsolete
//		String sessionId = getProject().getProperty(Constants.PROPERTY_SESSION_ID);
//		if (isNotNullNorEmpty(sessionId)) {
//			this.client = this.client.setSessionId(sessionId);
//		}
	}

	protected String getUserProperty() {
		return getProject().getProperty(Constants.PROPERTY_USER);
	}

	protected String getAppProperty() {
		if (getProject().getProperty(Constants.PROPERTY_APP) == null) {
			return getProject().getProperty(Constants.PROPERTY_PROJECT);
		} else {
			return getProject().getProperty(Constants.PROPERTY_APP);
		}
	}

	protected String getAppApkProperty() {
		return getProject().getProperty(Constants.PROPERTY_APP_APK);
	}

	protected String getTestApkProperty() {
		return getProject().getProperty(Constants.PROPERTY_TEST_APK);
	}

	private static boolean isNotNullNorEmpty(String value) {
		return value != null && value.trim().isEmpty() == false;
	}

}
