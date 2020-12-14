package ro.mihaisurdeanu.testing.framework;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/parallel/"},
        plugin = {
                "pretty", "json:target/all-parallel-tests.json",
                "html:target/all-parallel-tests.html",
                "ro.mihaisurdeanu.testing.framework.plugins.CustomPlugin"
        },
        extraGlue = {"ro.mihaisurdeanu.testing.framework.steps"},
        monochrome = true
)
public class AllParallelTests {

  // Run all scenarios in parallel using 2 threads

}
