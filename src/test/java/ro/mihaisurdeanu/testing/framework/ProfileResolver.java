package ro.mihaisurdeanu.testing.framework;

import org.jetbrains.annotations.NotNull;
import org.springframework.test.context.ActiveProfilesResolver;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public class ProfileResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(final @NotNull Class<?> clazz) {
        return new String[]{EnvironmentResolver.resolveBasedOnCucumberTag().getProfile()};
    }

}
