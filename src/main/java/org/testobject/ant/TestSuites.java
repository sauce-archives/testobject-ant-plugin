package org.testobject.ant;

import java.util.*;

public class TestSuites {

	public static class TestSuite {

		public static class TestCase {

			public static class Error {

				public final String type;
				public final String message;

				public Error(String type, String message) {
					super();
					this.type = type;
					this.message = message;
				}

				@Override
				public String toString() {
					return "\t\t\t" + String.format("<error type=\"%s\" message=\"%s\" />\n", type, message);
				}

			}

			public final String name;
			public final String status;
			public long time = 0;

			public final Set<Error> errors = new HashSet<Error>();

			public TestCase(String name, String status) {
				super();
				this.name = name;
				this.status = status;
			}

			@Override
			public String toString() {
				String starTag = "\t\t"
						+ String.format("<testcase name=\"%s\" time=\"%d\" status=\"%s\">\n", name, time, status);
				for (Error error : errors) {
					starTag += error.toString();
				}

				return starTag + "\t\t" + "</testcase>\n";
			}
		}

		public final String name;
		public long time = 0;
		public int tests = 0;
		public int errors = 0;

		public final Collection<TestCase> testCases = new ArrayList<TestCase>();

		public TestSuite(String name) {
			super();
			this.name = name;
		}

		@Override
		public String toString() {
			String starTag = "\t"
					+ String.format("<testsuite name=\"%s\" time=\"%d\" tests=\"%d\" errors=\"%d\">\n", name, time, tests, errors);
			for (TestCase testCase : testCases) {
				starTag += testCase.toString();
			}

			return starTag + "\t" + "</testsuite>\n";
		}

	}

	public final String name;
	public long time = 0;
	public int tests = 0;
	public int errors = 0;

	public final Collection<TestSuite> testSuites = new LinkedList<TestSuite>();

	public TestSuites(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		String starTag = String.format("<testsuites name=\"%s\" time=\"%d\" tests=\"%d\" errors=\"%d\">\n", name, time, tests, errors);
		for (TestSuite testSuite : testSuites) {
			starTag += testSuite.toString();
		}

		return starTag + "</testsuites>\n";
	}

}
