package ro.mihaisurdeanu.testing.framework;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/sequential/"},
        plugin = {
                "pretty",
                "json:target/all-sequential-tests.json",
                "html:target/all-sequential-tests.html",
                "timeline:target/timeline-sequential-report",
                "ro.mihaisurdeanu.testing.framework.plugin.CustomPlugin"
        },
        extraGlue = {"ro.mihaisurdeanu.testing.framework.step"},
        monochrome = true
)
public class AllSequentialTests {

  // Run all scenarios in sequential mode using a single thread

}
