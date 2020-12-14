package ro.mihaisurdeanu.testing.framework.steps;

import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import io.cucumber.java8.StepDefinitionBody;
import lombok.extern.slf4j.Slf4j;
import ro.mihaisurdeanu.testing.framework.AbstractSpringConfigTest;

@Slf4j
public class CommonStepDefinitions extends AbstractSpringConfigTest implements En {

  public CommonStepDefinitions() {
    Before(1, (Scenario scenario) -> scenarioSupportService.beforeScenarioRun(scenario.getId()));
    After(1, (Scenario scenario) -> scenarioSupportService.afterScenarioRun());

    Given("up and running testing engine", () -> log.info("Thread name = {}, Scenario ID = {}",
            Thread.currentThread().getName(), scenarioSupportService.getCurrentScenarioId()));

    When("sleep for \"{long}\" milliseconds", (StepDefinitionBody.A1<Long>) Thread::sleep);
  }

}
