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
        features = {"src/test/resources/features/parallel/"},
        plugin = {
                "pretty",
                "json:target/all-parallel-tests.json",
                "html:target/all-parallel-tests.html",
                "timeline:target/timeline-parallel-report",
                "ro.mihaisurdeanu.testing.framework.plugin.CustomPlugin"
        },
        extraGlue = {"ro.mihaisurdeanu.testing.framework.step"},
        monochrome = true
)
public class AllParallelTests {

  // Run all scenarios in parallel using 4 threads

}
