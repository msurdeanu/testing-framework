package ro.mihaisurdeanu.testing.framework.asserts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

import static org.assertj.core.api.Assertions.assertThat;

public final class GenericAsserts {

  public static final GenericAssert<JsonNode> NODE_EXISTS = (node) -> assertThat(node).isNotNull();

  public static final GenericAssert<JsonNode> NODE_IS_ARRAY = (node) -> assertThat(node.isArray()).isTrue();

  public static final GenericAssert<JsonNode> NODE_IS_INT = (node) -> assertThat(node).isInstanceOf(IntNode.class);

}
