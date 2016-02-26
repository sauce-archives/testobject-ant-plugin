package org.testobject.ant.client;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class TestObjectDevice {

	public enum OS { ANDROID, IOS }
	
	public final String id;
	public final String name;
	public final int apiLevel;
	public final OS os;
	public final String osVersion;
	public final boolean isAvailable;
	
	public TestObjectDevice(DeviceContainer deviceContainer, boolean isAvailable) {
		this.id = deviceContainer.id;
		this.name = deviceContainer.name;
		this.apiLevel = deviceContainer.apiLevel;
		this.os = deviceContainer.os;
		this.osVersion = deviceContainer.osVersion;
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return id + " (" + name + "), " + os + " (" + osVersion + "), api: " + apiLevel + ", " + (isAvailable ? "available" : "not available");
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeviceContainer {
		public String id;
		public String name;
		public int apiLevel;
		public OS os;
		public String osVersion;
	}
	
}
