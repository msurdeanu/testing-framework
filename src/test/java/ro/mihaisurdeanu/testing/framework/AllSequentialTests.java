package ro.mihaisurdeanu.testing.framework;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/sequential/"},
        plugin = {
                "pretty", "json:target/all-sequential-tests.json",
                "html:target/all-sequential-tests.html",
                "ro.mihaisurdeanu.testing.framework.plugins.CustomPlugin"
        },
        extraGlue = {"ro.mihaisurdeanu.testing.framework.steps"},
        monochrome = true
)
public class AllSequentialTests {

  // Run all scenarios in sequential mode using a single thread

}
