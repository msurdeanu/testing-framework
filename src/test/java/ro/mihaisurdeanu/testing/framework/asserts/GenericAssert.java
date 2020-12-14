package ro.mihaisurdeanu.testing.framework.asserts;

@FunctionalInterface
public interface GenericAssert<T> {

  void test(T object) throws Exception;

  default GenericAssert<T> and(GenericAssert<T> other) {
    return (object -> {
      test(object);
      other.test(object);
    });
  }

}
