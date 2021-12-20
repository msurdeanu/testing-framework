package ro.mihaisurdeanu.testing.framework.step;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.http.HttpMethod;
import ro.mihaisurdeanu.testing.framework.model.BlazeMeterDetails;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public class PerformanceStepDefinitions implements En {

    public PerformanceStepDefinitions(final SpringBaseTest baseTest) {
        DataTableType("[blank]", (Map<String, String> entry) -> BlazeMeterDetails.builder()
                .token(baseTest.resolvePlaceholders(entry.get("token")))
                .testName(baseTest.resolvePlaceholders(entry.get("testName")))
                .totalUsers(Integer.parseInt(baseTest.resolvePlaceholders(entry.get("totalUsers"))))
                .threadsPerEngine(Integer.parseInt(baseTest.resolvePlaceholders(entry.get("threadsPerEngine"))))
                .holdFor(Long.parseLong(baseTest.resolvePlaceholders(entry.get("holdFor"))))
                .testTimeout(Long.parseLong(baseTest.resolvePlaceholders(entry.get("testTimeout"))))
                .build());

        When("I setup a HTTP sampler \"{word}\" to", (String id, DataTable requestDetailsTable) -> {
            final HttpRequestDetails httpRequestDetails = requestDetailsTable.convert(HttpRequestDetails.class, true);
            baseTest.getPerformanceSupportService().createHttpSampler(id, httpRequestDetails);
        });

        When("I setup a thread group \"{word}\" with \"{int}\" threads and \"{int}\" iterations for each thread", (String id, Integer threads, Integer iterations) -> {
            baseTest.getPerformanceSupportService().createThreadGroup(id, threads, iterations);
        });

        When("I setup a local test plan \"{word}\" using thread group \"{word}\" and schedule it immediately", (String testPlanId, String threadGroupId) -> {
            final var dslThreadGroup = baseTest.getPerformanceSupportService().getThreadGroup(threadGroupId);
            assertThat(dslThreadGroup).isNotNull();

            baseTest.getPerformanceSupportService().createAndScheduleLocalTestPlan(testPlanId, dslThreadGroup);
        });

        When("I setup a BlazeMeter engine \"{word}\" to", (String id, DataTable blazeMeterEngineTable) -> {
            final BlazeMeterDetails blazeMeterDetails = blazeMeterEngineTable.convert(BlazeMeterDetails.class, true);
            baseTest.getPerformanceSupportService().createBlazeMeterEngine(id, blazeMeterDetails);
        });

        When("I setup a remote test plan \"{word}\" using thread group \"{word}\", engine \"{word}\" and schedule it immediately", (String testPlanId, String engineId, String threadGroupId) -> {
            final var dslThreadGroup = baseTest.getPerformanceSupportService().getThreadGroup(threadGroupId);
            assertThat(dslThreadGroup).isNotNull();

            final var dslJmeterEngine = baseTest.getPerformanceSupportService().getEngine(engineId);
            assertThat(dslJmeterEngine).isNotNull();

            baseTest.getPerformanceSupportService().createAndScheduleRemoteTestPlan(testPlanId, dslThreadGroup, dslJmeterEngine);
        });

        When("I inject HTTP sampler \"{word}\" into thread group \"{word}\"", (String httpSamplerId, String threadGroupId) -> {
            final var dslHttpSampler = baseTest.getPerformanceSupportService().getHttpSampler(httpSamplerId);
            assertThat(dslHttpSampler).isNotNull();

            final var dslThreadGroup = baseTest.getPerformanceSupportService().getThreadGroup(threadGroupId);
            assertThat(dslThreadGroup).isNotNull();

            dslThreadGroup.children(dslHttpSampler);
        });

        When("I inject custom regex extractor \"{word}\" into HTTP sampler \"{word}\" under name \"{word}\"", (String value, String httpSamplerId, String name) -> {
            final var dslHttpSampler = baseTest.getPerformanceSupportService().getHttpSampler(httpSamplerId);
            assertThat(dslHttpSampler).isNotNull();

            baseTest.getPerformanceSupportService().createRegexExtractor(dslHttpSampler, name, value);
        });

        Then("errors count for test plan \"{word}\" is equal to \"{long}\"", (String id, Long expected) -> {
            final var testPlanStats = baseTest.getPerformanceSupportService().getTestPlanStats(id);
            assertThat(testPlanStats).isNotNull();

            assertThat(testPlanStats.overall().errorsCount()).isEqualTo(expected);
        });
    }
}
