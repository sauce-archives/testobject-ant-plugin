package org.testobject.ant.client;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper = configureObjectMapper();

    public ObjectMapperProvider() {
    }

    private static ObjectMapper configureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(JsonMethod.CREATOR, Visibility.ANY)
                .setVisibility(JsonMethod.SETTER, Visibility.NONE)
                .setVisibility(JsonMethod.GETTER, Visibility.NONE)
                .setVisibility(JsonMethod.IS_GETTER, Visibility.NONE);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        mapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    public ObjectMapper getContext(Class<?> type) {
        return this.mapper;
    }
}
