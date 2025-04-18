package ro.mihaisurdeanu.testing.framework.util;

import java.util.regex.Matcher;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public interface StringReplacerCallback {

    String replace(Matcher matcher);

}
