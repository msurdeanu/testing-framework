package ro.mihaisurdeanu.testing.framework.services.internals;

import org.springframework.stereotype.Service;
import ro.mihaisurdeanu.testing.framework.services.StatefulSupportService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScenarioSupportService extends StatefulSupportService {

  private static final Map<String, String> scenarioIdsMap = new ConcurrentHashMap<>();

  public void beforeScenarioRun(String scenarioId) {
    scenarioIdsMap.put(Thread.currentThread().getName(), scenarioId);
  }

  public void afterScenarioRun() {
    scenarioIdsMap.remove(Thread.currentThread().getName());
    removeAllFromCache();
  }

  public String getCurrentScenarioId() {
    return scenarioIdsMap.getOrDefault(Thread.currentThread().getName(), "N/A");
  }

}
