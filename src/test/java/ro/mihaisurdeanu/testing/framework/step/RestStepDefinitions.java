package ro.mihaisurdeanu.testing.framework.step;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import ro.mihaisurdeanu.testing.framework.model.Constraint;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;
import ro.mihaisurdeanu.testing.framework.model.Operator;
import ro.mihaisurdeanu.testing.framework.serializer.JsonFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class RestStepDefinitions implements En {

    public RestStepDefinitions(final SpringBaseTest baseTest) {
        DataTableType("[blank]", (Map<String, String> entry) -> HttpRequestDetails.builder()
                .method(HttpMethod.valueOf(Optional.ofNullable(entry.get("method")).orElse("GET")))
                .url(baseTest.resolvePlaceholders(entry.get("url")))
                .body(baseTest.resolvePlaceholders(entry.get("body")))
                .headers(getHeadersAsMap(baseTest.resolvePlaceholders(entry.get("headers"))))
                .build());

        DataTableType("[blank]", (Map<String, String> entry) -> Constraint.builder()
                .expression(baseTest.resolvePlaceholders(entry.get("expression")))
                .operator(Operator.of(Optional.ofNullable(entry.get("operator")).orElse("=")))
                .expected(baseTest.resolvePlaceholders(entry.get("expected")))
                .build());

        When("I make HTTP request \"{word}\" with no authentication to", (String id, DataTable requestDetailsTable) -> {
            final HttpRequestDetails httpRequestDetails = requestDetailsTable.convert(HttpRequestDetails.class, true);
            baseTest.getHttpClientSupportService().doRequest(id, null, httpRequestDetails);
        });

        Then("status code for request \"{word}\" is \"{int}\"", (String id, Integer status) -> {
            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);

            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getStatusCode()).isEqualTo(status);
        });

        Then("response body for request \"{word}\" is a string", (String id) -> {
            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);

            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(String.class);
        });

        Then("response body for request \"{word}\" is string and contains {string}", (String id, String contains) -> {
            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);

            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(String.class);
            assertThat((String) httpClientResponse.getBody()).contains(contains);
        });

        Then("response body for request \"{word}\" is string and starts with {string}", (String id, String startsWith) -> {
            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);

            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(String.class);
            assertThat((String) httpClientResponse.getBody()).startsWith(startsWith);
        });

        Then("response body for request \"{word}\" is string and ends with {string}", (String id, String endsWith) -> {
            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);

            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(String.class);
            assertThat((String) httpClientResponse.getBody()).endsWith(endsWith);
        });

        Then("convert response body for request \"{word}\" to JSON format", (String id) -> {
            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);

            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(String.class);

            httpClientResponse.setBody(JsonFactory.fromJsonToDocumentContext((String) httpClientResponse.getBody()));
            assertThat(httpClientResponse.getBody()).isInstanceOf(DocumentContext.class);
        });

        Then("response body for request \"{word}\" is in JSON format and meets imposed constraints", (String id, DataTable constraintsTable) -> {
            final List<Constraint> constraints = constraintsTable.asList(Constraint.class);

            final var httpClientResponse = baseTest.getHttpClientSupportService().getClientResponse(id);
            assertThat(httpClientResponse).isNotNull();
            assertThat(httpClientResponse.getBody()).isInstanceOf(DocumentContext.class);

            constraints.forEach(constraint -> constraint.accept(((DocumentContext) httpClientResponse.getBody()).read(constraint.getExpression())));
        });
    }

    private Map<String, String> getHeadersAsMap(final String headers) {
        try {
            return JsonFactory.fromJsonToMap(Optional.ofNullable(headers).orElse("{}"));
        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON format for headers provided as input.", e);
        }

        return Map.of();
    }

}
