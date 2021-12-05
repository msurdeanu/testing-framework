package ro.mihaisurdeanu.testing.framework;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class EnvironmentResolver {

    public static EnvironmentTag resolveBasedOnCucumberTag() {
        final var environmentTag = EnvironmentTag.of(System.getProperty("cucumber.filter.tags", EnvironmentTag.INT.getValue()));
        log.info("Resolved profile is {}", environmentTag.getProfile());
        return environmentTag;
    }

    @RequiredArgsConstructor
    public enum EnvironmentTag {
        DEV("@Dev", "dev"),
        INT("@Int", "int"),
        PREPROD("@Preprod", "preprod"),
        PROD("@Prod", "prod");

        @Getter
        private final String value;

        @Getter
        private final String profile;

        public static EnvironmentTag of(final String value) {
            return Arrays.stream(EnvironmentTag.values())
                    .filter(item -> value != null && value.contains(item.getValue()))
                    .findFirst()
                    .orElse(EnvironmentTag.INT);
        }
    }
}
