package ro.mihaisurdeanu.testing.framework;

import org.jetbrains.annotations.NotNull;
import org.springframework.test.context.ActiveProfilesResolver;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public class ProfileResolver implements ActiveProfilesResolver {

    public static final String PRODUCTION_PROFILE = "production";
    public static final String NON_PRODUCTION_PROFILE = "non-production";

    @Override
    public String[] resolve(@NotNull Class<?> clazz) {
        final var environmentTag = EnvironmentResolver.resolveBasedOnCucumberTag();
        return new String[]{environmentTag.getProfile(),
                environmentTag == EnvironmentResolver.EnvironmentTag.PROD ? PRODUCTION_PROFILE : NON_PRODUCTION_PROFILE};
    }

}
