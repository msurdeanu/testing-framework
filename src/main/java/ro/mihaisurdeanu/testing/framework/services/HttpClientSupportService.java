package ro.mihaisurdeanu.testing.framework.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ro.mihaisurdeanu.testing.framework.aspects.ReadCache;
import ro.mihaisurdeanu.testing.framework.aspects.WriteCache;
import ro.mihaisurdeanu.testing.framework.models.HttpClientResponse;

import java.time.Duration;

@Slf4j
@Service
public class HttpClientSupportService extends StatefulSupportService {

  public static final RestTemplate DEFAULT_REST_TEMPLATE = buildDefaultRestTemplate();

  @ReadCache
  public HttpClientResponse getClientResponse(final String id) {
    throw new UnsupportedOperationException("Client response should be present in local cache every time.");
  }

  @WriteCache
  public HttpClientResponse makeGetRequest(final String id, final RestTemplate restTemplate, final String url) {
    HttpClientResponse.HttpClientResponseBuilder httpClientResponseBuilder = HttpClientResponse.builder();

    try {
      ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
      httpClientResponseBuilder.statusCode(responseEntity.getStatusCodeValue()).body(responseEntity.getBody());
    } catch (Exception exception) {
      if (exception instanceof HttpClientErrorException) {
        httpClientResponseBuilder.statusCode(((HttpClientErrorException) exception).getRawStatusCode());
      }

      httpClientResponseBuilder.exception(exception);
    }

    return httpClientResponseBuilder.build();
  }

  private static RestTemplate buildDefaultRestTemplate() {
    return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(60))
            .build();
  }

}
