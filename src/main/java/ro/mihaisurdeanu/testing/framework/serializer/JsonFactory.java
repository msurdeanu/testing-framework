package ro.mihaisurdeanu.testing.framework.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public final class JsonFactory {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static JsonNode toJsonNode(String content) throws IOException {
        return OBJECT_MAPPER.readTree(content);
    }

    public static Map<String, String> fromJsonToMap(@NonNull String body) throws JsonProcessingException {
        final TypeReference<Map<String, String>> typeReference = new TypeReference<>() {
        };

        return OBJECT_MAPPER.readValue(body, typeReference);
    }

    public static DocumentContext fromJsonToDocumentContext(@NonNull String body) {
        return JsonPath.parse(body);
    }

    public static String toString(final Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

}
