package ro.mihaisurdeanu.testing.framework.step;

import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import io.cucumber.java8.StepDefinitionBody;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class CommonStepDefinitions implements En {

    public CommonStepDefinitions(final SpringBaseTest baseTest) {
        Before(1, (Scenario scenario) -> baseTest.getScenarioSupportService().beforeScenarioRun(scenario.getId()));
        After(1, (Scenario scenario) -> baseTest.getScenarioSupportService().afterScenarioRun());

        Given("up and running testing engine", () -> log.info("Thread name = {}, Scenario ID = {}",
                Thread.currentThread().getName(), baseTest.getScenarioSupportService().getCurrentScenarioId()));

        When("sleep for \"{long}\" milliseconds", (StepDefinitionBody.A1<Long>) Thread::sleep);
    }

}
