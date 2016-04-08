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

		String app = getAppProperty();
		String appApk = getAppApkProperty();
		String testApk = getTestApkProperty();
		Long testSuite = Long.parseLong(getTestSuiteProperty());
		String team = getTeamProperty();
		Boolean runAsPackage = Boolean.parseBoolean(getRunAsPackageProperty());

		File appApkFile = new File(appApk);
		File testApkFile = new File(testApk);

		TestObjectClient client = (TestObjectClient) PropertyHelper.getProperty(getProject(), "org.testobject.client");

		TestSuiteResource.InstrumentationTestSuiteRequest instrumentationTestSuiteRequest = new TestSuiteResource.InstrumentationTestSuiteRequest(runAsPackage);

		try {
			client.updateInstrumentationTestSuite(team, app, testSuite, appApkFile, testApkFile, instrumentationTestSuiteRequest);
			log(String.format("Uploaded: \nappAPK: %s \ntestAPK: %s", appApkFile.getAbsolutePath(), testApkFile.getAbsolutePath()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(String.format("Unable to update testSuite %s ", testSuite));
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

}