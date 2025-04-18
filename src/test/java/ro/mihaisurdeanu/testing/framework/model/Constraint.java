package ro.mihaisurdeanu.testing.framework.model;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Consumer;

import static java.lang.String.format;

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
    public void accept(Object object) {
        switch (object) {
            case Integer intObj ->
                    operator.assertValueAgainstExpected(intObj, Integer.parseInt(expected), getDescription());
            case Float floatObj ->
                    operator.assertValueAgainstExpected(floatObj, Float.parseFloat(expected), getDescription());
            case Double doubleObj ->
                    operator.assertValueAgainstExpected(doubleObj, Double.parseDouble(expected), getDescription());
            default -> operator.assertValueAgainstExpected(object.toString(), expected, getDescription());
        }
    }

    private String getDescription() {
        return format("check constraint expression '%s' and operator '%s'", expression, operator.getValue());
    }

}
