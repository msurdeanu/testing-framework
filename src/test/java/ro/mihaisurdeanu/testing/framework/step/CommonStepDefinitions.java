package ro.mihaisurdeanu.testing.framework.step;

import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import io.cucumber.java8.StepDefinitionBody;
import lombok.extern.slf4j.Slf4j;
import ro.mihaisurdeanu.testing.framework.service.ScenarioSupportService;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class CommonStepDefinitions implements En {

    public CommonStepDefinitions(final SpringBaseTest baseTest) {
        Before(1, (Scenario scenario) -> baseTest.getService(ScenarioSupportService.class).beforeScenarioRun(scenario.getId()));
        After(1, (Scenario scenario) -> baseTest.getService(ScenarioSupportService.class).afterScenarioRun());

        Given("up and running testing engine", () -> log.info("Thread name = {}, Scenario ID = {}",
                Thread.currentThread().getName(), baseTest.getService(ScenarioSupportService.class).getCurrentScenarioId()));

        When("sleep for \"{long}\" milliseconds", (StepDefinitionBody.A1<Long>) Thread::sleep);
    }

}
