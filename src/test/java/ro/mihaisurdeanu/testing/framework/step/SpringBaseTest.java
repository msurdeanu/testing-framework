package ro.mihaisurdeanu.testing.framework.step;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import ro.mihaisurdeanu.testing.framework.Application;
import ro.mihaisurdeanu.testing.framework.exception.TestingException;
import ro.mihaisurdeanu.testing.framework.service.ScenarioSupportService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;
import static ro.mihaisurdeanu.testing.framework.util.StringReplacer.replace;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBaseTest {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\}");

    @LocalServerPort
    public int serverPort;

    @Autowired
    private ApplicationContext applicationContext;

    public <T> T getService(final Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public String resolvePlaceholders(final String value) {
        return ofNullable(value)
                .map(item -> replace(item, PLACEHOLDER_PATTERN, this::matcherToStringCallback))
                .orElse(null);
    }

    private String matcherToStringCallback(final Matcher matcher) {
        final var placeholderName = matcher.group(1);
        return ofNullable(getService(ScenarioSupportService.class).getFromCache(placeholderName))
                .map(Object::toString)
                .orElseThrow(() -> new TestingException("No entry found in test local cache for placeholder: " + placeholderName));
    }

}
