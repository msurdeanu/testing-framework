package ro.mihaisurdeanu.testing.framework.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HttpClientResponse {

  private int statusCode;
  private Object body;
  private Exception exception;

}
