package ro.mihaisurdeanu.testing.framework.step;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
public class PerformanceStepDefinitions implements En {

    public PerformanceStepDefinitions(final SpringBaseTest baseTest) {
        When("I setup a HTTP sampler \"{word}\" to", (String id, DataTable requestDetailsTable) -> {
            final HttpRequestDetails httpRequestDetails = requestDetailsTable.convert(HttpRequestDetails.class, true);
            baseTest.getPerformanceSupportService().createHttpSampler(id, httpRequestDetails);
        });

        When("I setup a thread group \"{word}\" with \"{int}\" threads and \"{int}\" iterations for each thread", (String id, Integer threads, Integer iterations) -> {
            baseTest.getPerformanceSupportService().createThreadGroup(id, threads, iterations);
        });

        When("I setup a test plan \"{word}\" using thread group \"{word}\" and schedule it immediately", (String testPlanId, String threadGroupId) -> {
            final var dslThreadGroup = baseTest.getPerformanceSupportService().getThreadGroup(threadGroupId);
            assertThat(dslThreadGroup).isNotNull();

            baseTest.getPerformanceSupportService().createAndScheduleTestPlan(testPlanId, dslThreadGroup);
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