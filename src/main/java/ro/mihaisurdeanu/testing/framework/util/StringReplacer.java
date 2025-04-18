package ro.mihaisurdeanu.testing.framework.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringReplacer {

    public static String replace(String value, Pattern regex, StringReplacerCallback callback) {
        final var builder = new StringBuilder();
        final var regexMatcher = regex.matcher(value);
        while (regexMatcher.find()) {
            regexMatcher.appendReplacement(builder, callback.replace(regexMatcher));
        }
        regexMatcher.appendTail(builder);
        return builder.toString();
    }

}
