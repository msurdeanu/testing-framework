package ro.mihaisurdeanu.testing.framework.model;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Builder
@Getter
public class Constraint implements Consumer<Object> {

    private String expression;
    private Operator operator;
    private String expected;

    @Override
    public void accept(final Object object) {
        if (object instanceof Integer) {
            operator.assertValueAgainstExpected((Integer) object, Integer.parseInt(expected), getDescription());
        } else {
            operator.assertValueAgainstExpected(object.toString(), expected, getDescription());
        }
    }

    private String getDescription() {
        return String.format("check constraint expression '%s' and operator '%s'", expression, operator.getValue());
    }

}
