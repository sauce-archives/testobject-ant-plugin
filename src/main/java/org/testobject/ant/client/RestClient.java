package org.testobject.ant.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class RestClient {
	private final WebResource resource;
	private final Map<String, Cookie> cookies = new HashMap();

	public RestClient(String url) {
		DefaultClientConfig config = new DefaultClientConfig(new Class[]{ObjectMapperProvider.class});
		config.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE);
		config.getClasses().add(JacksonJsonProvider.class);
		this.resource = Client.create(config).resource(URI.create(url));
	}

	public void setCookie(String key, String value) {
		this.cookies.put(key, new Cookie(key, value));
	}

	public String getCookie(String key) {
		return ((Cookie)this.cookies.get(key)).getValue();
	}

	public Builder plain(String path) {
		Builder builder = this.resource.path(path).getRequestBuilder();

		Cookie cookie;
		for(Iterator var4 = this.cookies.values().iterator(); var4.hasNext(); builder = (Builder)builder.cookie(cookie)) {
			cookie = (Cookie)var4.next();
		}

		return builder;
	}

	public Builder resource(String... path) {
		return (Builder)((Builder)this.plain(this.toString(path)).type("application/json")).accept(new String[]{"application/json"});
	}

	public Builder form(String... path) {
		return (Builder)this.plain(this.toString(path)).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
	}

	private String toString(String[] pathElements) {
		if(pathElements == null) {
			return "";
		} else {
			String result = "";

			for(int i = 0; i < pathElements.length - 1; ++i) {
				result = result + pathElements[i] + "/";
			}

			return result + pathElements[pathElements.length - 1];
		}
	}
}
