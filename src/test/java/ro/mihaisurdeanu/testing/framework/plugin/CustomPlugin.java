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
import java.util.UUID;

import static java.util.Optional.ofNullable;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
public class CustomPlugin implements Plugin, ConcurrentEventListener {

    private static final Map<UUID, Instant> TEST_CASE_MAP = new HashMap<>();

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
    }

    private void handleTestCaseStarted(TestCaseStarted testCaseStarted) {
        TEST_CASE_MAP.put(testCaseStarted.getTestCase().getId(), testCaseStarted.getInstant());
    }

    private void handleTestCaseFinished(TestCaseFinished testCaseFinished) {
        final var testCaseId = testCaseFinished.getTestCase().getId();
        ofNullable(TEST_CASE_MAP.remove(testCaseId))
                .ifPresent(time -> log.info("Test scenario with ID = {} took {} ms to be executed.",
                        testCaseId, Duration.between(time, testCaseFinished.getInstant()).toMillis()));
    }

}
