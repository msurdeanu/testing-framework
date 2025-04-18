package ro.mihaisurdeanu.testing.framework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ro.mihaisurdeanu.testing.framework.aop.ReadCache;
import ro.mihaisurdeanu.testing.framework.aop.WriteCache;
import ro.mihaisurdeanu.testing.framework.model.HttpClientResponse;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;

import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
@Lazy
@Service
@RequiredArgsConstructor
public class HttpClientSupportService extends StatefulSupportService {

    private final RestTemplate restTemplate;

    @ReadCache
    public HttpClientResponse getClientResponse(String id) {
        throw new IllegalArgumentException("No HttpClientResponse could be found in test local cache after id = " + id);
    }

    @WriteCache
    public HttpClientResponse doRequest(String id, @Nullable RestTemplate restTemplate, HttpRequestDetails httpRequestDetails) {
        final var httpClientResponseBuilder = HttpClientResponse.builder();

        try {
            final var responseEntity = ofNullable(restTemplate).orElse(this.restTemplate)
                    .exchange(fromUriString(httpRequestDetails.getUrl()).build(true).toUri(),
                            httpRequestDetails.getMethod(),
                            getHttpEntity(httpRequestDetails.getBody(), httpRequestDetails.getHeaders()),
                            Resource.class);
            httpClientResponseBuilder
                    .statusCode(responseEntity.getStatusCodeValue())
                    .body(responseEntity.getBody())
                    .headers(responseEntity.getHeaders());
        } catch (HttpStatusCodeException httpStatusCodeException) {
            log.warn("An exception occurred during HTTP request.", httpStatusCodeException);
            httpClientResponseBuilder
                    .statusCode(httpStatusCodeException.getRawStatusCode())
                    .body(httpStatusCodeException.getResponseBodyAsString())
                    .headers(httpStatusCodeException.getResponseHeaders());
        }

        return httpClientResponseBuilder.build();
    }

    private HttpEntity<String> getHttpEntity(String body, Map<String, String> extraHeaders) {
        final var headers = new HttpHeaders();
        extraHeaders.forEach(headers::set);

        return new HttpEntity<>(body, headers);
    }

}
