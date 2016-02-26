package org.testobject.ant;

public class Constants {

	public static final String PROPERTY_SSL = "testobject.http.ssl";
	public static final String PROPERTY_ENDPOINT = "testobject.http.endpoint";

	public static final String PROPERTY_SESSION_ID = "testobject.auth.session.id";

	public static final String PROPERTY_USER = "testobject.user";
	public static final String PROPERTY_PROJECT = "testobject.project"; // deprecated - replaced by app
	public static final String PROPERTY_APP = "testobject.app";
	public static final String PROPERTY_APP_APK = "testobject.apk.app";
	public static final String PROPERTY_TEST_APK = "testobject.apk.test";

	public static final String PROPERTY_BATCH_STATUS = "testobject.batch.status"; // deprecated - replaced by testSuiteStatus
	public static final String PROPERTY_BATCH_TESTS = "testobject.batch.tests"; // deprecated - replaced by testSuiteTests
	public static final String PROPERTY_BATCH_ERRORS = "testobject.batch.errors"; // deprecated - replaced by testSuiteErrors

	public static final String PROPERTY_TEST_SUITE_STATUS = "testobject.testSuite.status";
	public static final String PROPERTY_TEST_SUITE_TESTS = "testobject.testSuite.tests";
	public static final String PROPERTY_TEST_SUITE_ERRORS = "testobject.testSuite.errors";

	public static final String PROPERTY_VERSION_NEW = "testobject.version.new";

	public static final String PROPERTY_BATCH_REPORT_NEW = "testobject.batch.report.new"; // deprecated - replaced by suiteReportNew
	public static final String PROPERTY_SUITE_REPORT_NEW = "testobject.suite.report.new";
}
