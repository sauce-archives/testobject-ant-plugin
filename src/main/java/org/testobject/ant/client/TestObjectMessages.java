package org.testobject.ant.client;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collection;
import java.util.List;

public interface TestObjectMessages {

	public static class Meta {
		public final String eventName;
		public final int eventId;

		public Meta(@JsonProperty("eventName") String eventName, @JsonProperty("eventId") int eventId) {
			this.eventName = eventName;
			this.eventId = eventId;
		}
	}

	public static class CreateVersion {
		public final String userId;
		public final String projectId;
		public final String uploadId;

		public CreateVersion(String userId, String projectId, String uploadId) {
			this.userId = userId;
			this.projectId = projectId;
			this.uploadId = uploadId;
		}
	}

	public static class RobotiumBatchRequestData {

		public static class RobotiumBatch {

			public final String type = "ROBOTIUM";
			public final List<String> devices;
			public final String name;
			public final String scheduling = "MANUALLY";
			public final String networkSpeed = "FULL";

			public RobotiumBatch(String name, List<String> devices) {
				this.name = name;
				this.devices = devices;
			}
		}

		public final RobotiumBatch batch;
		public final String uploadId;

		@JsonCreator
		public RobotiumBatchRequestData(RobotiumBatch batch, String uploadId) {
			this.batch = batch;
			this.uploadId = uploadId;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UpdateRobotiumBatchApkResponse {

		public final long id;

		@JsonCreator
		public UpdateRobotiumBatchApkResponse(@JsonProperty("id") long id) {
			this.id = id;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CreateVersionResponse {

		public final int id;
		public final String name;
		public final String type;
		public final String url;

		@JsonCreator
		public CreateVersionResponse(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type,
				@JsonProperty("url") String url) {
			this.id = id;
			this.name = name;
			this.type = type;
			this.url = url;
		}

		@Override
		public String toString() {
			return "CreateVersionResponse [id=" + id + ", name=" + name + ", type=" + type + ", url=" + url + "]";
		}

	}

	public static class SetDefaultVersion {
		public final String userId;
		public final String projectId;
		public final int appId;

		public SetDefaultVersion(String userId, String projectId, int appId) {
			this.userId = userId;
			this.projectId = projectId;
			this.appId = appId;
		}
	}

	public static class SetDefaultVersionResponse {}

	public static class StartBatch {
		public final String userId;
		public final String projectId;
		public final int batchId;

		public StartBatch(String userId, String projectId, int batchId) {
			this.userId = userId;
			this.projectId = projectId;
			this.batchId = batchId;
		}
	}

	public static class NativeApp {

		public final String name;
		public final String uploadId;
		public final String usage = "TESTING";
		public final String type;
		public final long creationTime;

		public NativeApp(String name, String uploadId, long creationTime) {
			this.name = name;
			this.uploadId = uploadId;
			this.creationTime = creationTime;
			this.type = "NATIVE";
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Project {

		public final String name;
		public long defaultApp;
		public boolean readOnly;
		public final String displayName;
		public boolean archived;
		public Collection<String> scenarios;
		public String hockeyAppToken;
		public String hockeyAppWebhookToken;
		public String githubAccessToken;
		public String githubStateToken;
		public String githubRepositoryId;

		@JsonCreator
		public Project(@JsonProperty("name") String name,
				@JsonProperty("readOnly") boolean readOnly,
				@JsonProperty("displayName") String displayName,
				@JsonProperty("archived") boolean archived,
				@JsonProperty("scenarios") Collection<String> scenarios,
				@JsonProperty("hockeyAppToken") String hockeyAppToken,
				@JsonProperty("hockeyAppWebhookToken") String hockeyAppWebhookToken,
				@JsonProperty("githubAccessToken") String githubAccessToken,
				@JsonProperty("githubStateToken") String githubStateToken,
				@JsonProperty("githubRepositoryId") String githubRepositoryId) {
			this.hockeyAppToken = hockeyAppToken;
			this.readOnly = readOnly;
			this.name = name;
			this.displayName = displayName;
			this.archived = archived;
			this.scenarios = scenarios;
			this.hockeyAppWebhookToken = hockeyAppWebhookToken;
			this.githubAccessToken = githubAccessToken;
			this.githubStateToken = githubStateToken;
			this.githubRepositoryId = githubRepositoryId;
		}

	}

}
