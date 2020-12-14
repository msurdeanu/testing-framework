package ro.mihaisurdeanu.testing.framework;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import ro.mihaisurdeanu.testing.framework.services.HttpClientSupportService;
import ro.mihaisurdeanu.testing.framework.services.internals.ScenarioSupportService;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractSpringConfigTest {

  @LocalServerPort
  protected int serverPort;

  @Autowired
  protected HttpClientSupportService httpClientSupportService;

  @Autowired
  protected ScenarioSupportService scenarioSupportService;

}
