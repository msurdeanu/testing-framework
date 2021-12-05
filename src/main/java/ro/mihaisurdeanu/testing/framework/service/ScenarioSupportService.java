package ro.mihaisurdeanu.testing.framework.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ro.mihaisurdeanu.testing.framework.service.StatefulSupportService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Lazy
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
