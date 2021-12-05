package ro.mihaisurdeanu.testing.framework.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum Operator {

    EQUAL_TO("=") {
        public void assertValueAgainstExpected(final Integer value, final Integer expected, final String description) {
            assertThat(value).as(description).isEqualTo(expected);
        }

        public void assertValueAgainstExpected(final String value, final String expected, final String description) {
            assertThat(value).as(description).isEqualTo(expected);
        }
    },
    NOT_EQUAL_TO("<>") {
        public void assertValueAgainstExpected(final Integer value, final Integer expected, final String description) {
            assertThat(value).as(description).isNotEqualTo(expected);
        }

        public void assertValueAgainstExpected(final String value, final String expected, final String description) {
            assertThat(value).as(description).isNotEqualTo(expected);
        }
    };

    @Getter
    private final String value;

    public abstract void assertValueAgainstExpected(final Integer value, final Integer expected, final String description);

    public abstract void assertValueAgainstExpected(final String value, final String expected, final String description);

    public static Operator of(final String value) {
        return Arrays.stream(Operator.values())
                .filter(operator -> operator.getValue().equals(value))
                .findFirst()
                .orElse(Operator.EQUAL_TO);
    }

}
