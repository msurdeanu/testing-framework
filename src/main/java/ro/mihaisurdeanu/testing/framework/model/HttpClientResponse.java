package ro.mihaisurdeanu.testing.framework.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Builder
@Getter
@Setter
public class HttpClientResponse {

    private int statusCode;
    private Object body;
    private HttpHeaders headers;

}
