package ro.mihaisurdeanu.testing.framework.plugin;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.Plugin;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class CustomPlugin implements Plugin, ConcurrentEventListener {

    private static final Map<String, Instant> TEST_CASE_MAP = new HashMap<>();

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
    }

    private void handleTestCaseStarted(TestCaseStarted testCaseStarted) {
        TEST_CASE_MAP.put(testCaseStarted.getTestCase().getId().toString(), testCaseStarted.getInstant());
    }

    private void handleTestCaseFinished(TestCaseFinished testCaseFinished) {
        final String testCaseId = testCaseFinished.getTestCase().getId().toString();
        Instant startTime = TEST_CASE_MAP.remove(testCaseId);
        if (startTime == null) {
            return;
        }

        log.info("Test scenario with ID = {} took {} milliseconds to be executed. This information is available by listening to internal events.",
                testCaseId, Duration.between(startTime, testCaseFinished.getInstant()).toMillis());
    }

}
