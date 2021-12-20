package ro.mihaisurdeanu.testing.framework.model;

import lombok.Builder;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Builder
@Getter
public class BlazeMeterDetails implements HasValidation {

    private String token;

    private String testName;

    private int totalUsers = 1;

    private int threadsPerEngine = 1;

    private int iterations = 1;

    private long holdFor = 60;

    private long testTimeout = 60;

    @Override
    public void validate() {
        requireNonNull(token, "token cannot be null");
        requireNonNull(testName, "testName cannot be null");
    }

}
