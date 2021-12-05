package ro.mihaisurdeanu.testing.framework.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Getter
@Builder
public class HttpRequestDetails implements HasValidation {

    private HttpMethod method;

    private String url;

    private String body;

    private Map<String, String> headers;

    @Override
    public void validate() {
        requireNonNull(method, "method cannot be null");
        requireNonNull(url, "url cannot be null");
    }
}
