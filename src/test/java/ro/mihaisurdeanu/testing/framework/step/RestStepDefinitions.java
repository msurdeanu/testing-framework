package ro.mihaisurdeanu.testing.framework.step;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ro.mihaisurdeanu.testing.framework.model.Constraint;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;
import ro.mihaisurdeanu.testing.framework.model.Operator;
import ro.mihaisurdeanu.testing.framework.serializer.JsonFactory;
import ro.mihaisurdeanu.testing.framework.service.HttpClientSupportService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static ro.mihaisurdeanu.testing.framework.serializer.JsonFactory.fromJsonToMap;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class RestStepDefinitions implements En {

    public RestStepDefinitions(SpringBaseTest baseTest) {
        DataTableType("[blank]", (Map<String, String> entry) -> HttpRequestDetails.builder()
                .method(HttpMethod.valueOf(ofNullable(entry.get("method")).orElse("GET")))
                .url(baseTest.resolvePlaceholders(entry.get("url")))
                .body(baseTest.resolvePlaceholders(entry.get("body")))
                .headers(getHeadersAsMap(baseTest.resolvePlaceholders(entry.get("headers"))))
                .build());

        DataTableType("[blank]", (Map<String, String> entry) -> Constraint.builder()
                .expression(baseTest.resolvePlaceholders(entry.get("expression")))
                .operator(Operator.of(ofNullable(entry.get("operator")).orElse("=")))
                .expected(baseTest.resolvePlaceholders(entry.get("expected")))
                .build());

        When("I make HTTP request \"{word}\" with no authentication to", (String id, DataTable requestDetailsTable) -> {
            final HttpRequestDetails httpRequestDetails = requestDetailsTable.convert(HttpRequestDetails.class, true);
            baseTest.getService(HttpClientSupportService.class).doRequest(id, null, httpRequestDetails);
        });

        Then("status code for request \"{word}\" is \"{int}\"", (String id, Integer status) -> {
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getStatusCode()).isEqualTo(status);
        });

        Then("response headers for request \"{word}\" contains key \"{word}\" equal to \"{word}\"", (String id, String key, String value) -> {
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();

            final var headers = httpClientResponse.getHeaders();
            assertThat(headers).isNotNull();
            assertThat(headers.getFirst(key)).isEqualTo(baseTest.resolvePlaceholders(value));
        });

        Then("response body for request \"{word}\" is string and contains {string}", (String id, String contains) -> {
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();

            final var body = httpClientResponse.getBody();
            assertThat(body).isInstanceOf(Resource.class);

            final var bodyContent = decryptBodyIfNeeded((Resource) body, httpClientResponse.getHeaders());
            assertThat(bodyContent).contains(contains);
        });

        Then("response body for request \"{word}\" is string and starts with {string}", (String id, String startsWith) -> {
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();

            final var body = httpClientResponse.getBody();
            assertThat(body).isInstanceOf(Resource.class);

            final var bodyContent = decryptBodyIfNeeded((Resource) body, httpClientResponse.getHeaders());
            assertThat(bodyContent).startsWith(startsWith);
        });

        Then("response body for request \"{word}\" is string and ends with {string}", (String id, String endsWith) -> {
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();
            final var body = httpClientResponse.getBody();
            assertThat(body).isInstanceOf(Resource.class);
            final var bodyContent = decryptBodyIfNeeded((Resource) body, httpClientResponse.getHeaders());
            assertThat(bodyContent).endsWith(endsWith);
        });

        Then("convert response body for request \"{word}\" to JSON format", (String id) -> {
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();

            final var body = httpClientResponse.getBody();
            assertThat(body).isInstanceOf(Resource.class);

            final var bodyContent = decryptBodyIfNeeded((Resource) body, httpClientResponse.getHeaders());
            httpClientResponse.setBody(JsonFactory.fromJsonToDocumentContext(bodyContent));
            assertThat(httpClientResponse.getBody()).isInstanceOf(DocumentContext.class);
        });

        Then("response body for request \"{word}\" is in JSON format and meets imposed constraints", (String id, DataTable constraintsTable) -> {
            final List<Constraint> constraints = constraintsTable.asList(Constraint.class);
            final var httpClientResponse = baseTest.getService(HttpClientSupportService.class).getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(DocumentContext.class);
            constraints.forEach(constraint -> constraint.accept(((DocumentContext) httpClientResponse.getBody()).read(constraint.getExpression())));
        });
    }

    private Map<String, String> getHeadersAsMap(final String headers) {
        try {
            return fromJsonToMap(ofNullable(headers).orElse("{}"));
        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON format for headers provided as input.", e);
        }

        return Map.of();
    }

    private String decryptBodyIfNeeded(final Resource resource, HttpHeaders headers) throws IOException {
        final var contentEncoding = headers.getFirst(HttpHeaders.CONTENT_ENCODING);

        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            try (final var gzipInputStream = new GZIPInputStream(resource.getInputStream())) {
                return new String(gzipInputStream.readAllBytes());
            }
        } else {
            return new String(resource.getInputStream().readAllBytes());
        }
    }

}
