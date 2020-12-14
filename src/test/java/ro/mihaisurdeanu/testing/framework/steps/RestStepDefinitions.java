package ro.mihaisurdeanu.testing.framework.steps;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java8.En;
import ro.mihaisurdeanu.testing.framework.AbstractSpringConfigTest;
import ro.mihaisurdeanu.testing.framework.asserts.GenericAsserts;
import ro.mihaisurdeanu.testing.framework.models.HttpClientResponse;
import ro.mihaisurdeanu.testing.framework.serializers.JsonFactory;
import ro.mihaisurdeanu.testing.framework.services.HttpClientSupportService;

import static org.assertj.core.api.Assertions.assertThat;

public class RestStepDefinitions extends AbstractSpringConfigTest implements En {

  public RestStepDefinitions() {
    When("I make a GET request \"{word}\" to URL \"{word}\" with no authentication", (String id, String url) ->
            httpClientSupportService.makeGetRequest(id, HttpClientSupportService.DEFAULT_REST_TEMPLATE, url));

    Then("status code for request \"{word}\" is \"{int}\"", (String id, Integer status) -> {
      final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

      assertThat(clientResponse).isNotNull();
      assertThat(clientResponse.getStatusCode()).isEqualTo(status);
    });

    Then("response body for request \"{word}\" is a string", (String id) -> {
      final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

      assertThat(clientResponse).isNotNull();
      assertThat(clientResponse.getBody()).isInstanceOf(String.class);
    });

    Then("response body for request \"{word}\" is string and contains \"{word}\"", (String id, String value) -> {
      final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

      assertThat(clientResponse).isNotNull();
      assertThat(clientResponse.getBody()).isInstanceOf(String.class);
      assertThat((String) clientResponse.getBody()).contains(value);
    });

    Then("response body for request \"{word}\" is string and starts with \"{word}\"", (String id, String value) -> {
      final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

      assertThat(clientResponse).isNotNull();
      assertThat(clientResponse.getBody()).isInstanceOf(String.class);
      assertThat((String) clientResponse.getBody()).startsWith(value);
    });

    Then("response body for request \"{word}\" is string and ends with \"{word}\"", (String id, String value) -> {
      final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

      assertThat(clientResponse).isNotNull();
      assertThat(clientResponse.getBody()).isInstanceOf(String.class);
      assertThat((String) clientResponse.getBody()).endsWith(value);
    });

    Then("deserialize response body for request \"{word}\" to JSON format", (String id) -> {
      final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

      assertThat(clientResponse).isNotNull();
      assertThat(clientResponse.getBody()).isInstanceOf(String.class);

      clientResponse.setBody(JsonFactory.toJsonNode((String) clientResponse.getBody()));
      assertThat(clientResponse.getBody()).isInstanceOf(JsonNode.class);
    });

    Then("response body for request \"{word}\" is in JSON format and root node is array", (String id) ->
            GenericAsserts.NODE_IS_ARRAY
                    .test(extractNodeFromHttpResponse(id, "/")));

    Then("response body for request \"{word}\" is in JSON format, root node is array and has size = \"{int}\"", (String id, Integer size) ->
            GenericAsserts.NODE_IS_ARRAY
                    .and((node) -> assertThat(node).hasSize(size))
                    .test(extractNodeFromHttpResponse(id, "/")));
  }

  private JsonNode extractNodeFromHttpResponse(final String id, final String fieldPointer) {
    final HttpClientResponse clientResponse = httpClientSupportService.getClientResponse(id);

    assertThat(clientResponse).isNotNull();
    assertThat(clientResponse.getBody()).isInstanceOf(JsonNode.class);

    return ((JsonNode) clientResponse.getBody()).at(fieldPointer);
  }

}
