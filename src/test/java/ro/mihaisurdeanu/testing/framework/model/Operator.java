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

    STRICT_LESS_THEN("<") {
        public void assertValueAgainstExpected(Integer value, Integer expected, String description) {
            assertThat(value).as(description).isLessThan(expected);
        }

        public void assertValueAgainstExpected(Float value, Float expected, String description) {
            assertThat(value).as(description).isLessThan(expected);
        }

        public void assertValueAgainstExpected(Double value, Double expected, String description) {
            assertThat(value).as(description).isLessThan(expected);
        }

        public void assertValueAgainstExpected(String value, String expected, String description) {
            assertThat(value).as(description).isLessThan(expected);
        }
    },
    EQUAL_TO("=") {
        public void assertValueAgainstExpected(Integer value, Integer expected, String description) {
            assertThat(value).as(description).isEqualTo(expected);
        }

        public void assertValueAgainstExpected(Float value, Float expected, String description) {
            assertThat(value).as(description).isEqualTo(expected);
        }

        public void assertValueAgainstExpected(Double value, Double expected, String description) {
            assertThat(value).as(description).isEqualTo(expected);
        }

        public void assertValueAgainstExpected(String value, String expected, String description) {
            assertThat(value).as(description).isEqualTo(expected);
        }
    },
    STRICT_GREATER_THEN(">") {
        public void assertValueAgainstExpected(Integer value, Integer expected, String description) {
            assertThat(value).as(description).isGreaterThan(expected);
        }

        public void assertValueAgainstExpected(Float value, Float expected, String description) {
            assertThat(value).as(description).isGreaterThan(expected);
        }

        public void assertValueAgainstExpected(Double value, Double expected, String description) {
            assertThat(value).as(description).isGreaterThan(expected);
        }

        public void assertValueAgainstExpected(String value, String expected, String description) {
            assertThat(value).as(description).isGreaterThan(expected);
        }
    },
    NOT_EQUAL_TO("<>") {
        public void assertValueAgainstExpected(Integer value, Integer expected, String description) {
            assertThat(value).as(description).isNotEqualTo(expected);
        }

        public void assertValueAgainstExpected(Float value, Float expected, String description) {
            assertThat(value).as(description).isNotEqualTo(expected);
        }

        public void assertValueAgainstExpected(Double value, Double expected, String description) {
            assertThat(value).as(description).isNotEqualTo(expected);
        }

        public void assertValueAgainstExpected(String value, String expected, String description) {
            assertThat(value).as(description).isNotEqualTo(expected);
        }
    };

    @Getter
    private final String value;

    public abstract void assertValueAgainstExpected(Integer value, Integer expected, String description);

    public abstract void assertValueAgainstExpected(Float value, Float expected, String description);

    public abstract void assertValueAgainstExpected(Double value, Double expected, String description);

    public abstract void assertValueAgainstExpected(String value, String expected, String description);

    public static Operator of(String value) {
        return Arrays.stream(Operator.values())
                .filter(operator -> operator.getValue().equals(value))
                .findFirst()
                .orElse(Operator.EQUAL_TO);
    }

}
