package ro.mihaisurdeanu.testing.framework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.mihaisurdeanu.testing.framework.aop.ReadCache;
import ro.mihaisurdeanu.testing.framework.aop.WriteCache;
import ro.mihaisurdeanu.testing.framework.model.BlazeMeterDetails;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;
import us.abstracta.jmeter.javadsl.blazemeter.BlazeMeterEngine;
import us.abstracta.jmeter.javadsl.core.DslJmeterEngine;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslDefaultThreadGroup;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslThreadGroup;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static java.util.Optional.ofNullable;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Slf4j
@Service
public class PerformanceSupportService extends StatefulSupportService {

    @ReadCache
    public TestPlanStats getTestPlanStats(final String id) {
        throw new IllegalArgumentException("No TestPlanStats could be found in test local cache after id = " + id);
    }

    @WriteCache
    public TestPlanStats createAndScheduleLocalTestPlan(final String id,
                                                        final DslThreadGroup dslThreadGroup) throws IOException {
        return testPlan(dslThreadGroup, jtlWriter("target", "test-" + id + ".jtl"))
                .run();
    }

    @WriteCache
    public TestPlanStats createAndScheduleRemoteTestPlan(final String id,
                                                         final DslThreadGroup dslThreadGroup,
                                                         final DslJmeterEngine dslJmeterEngine) throws IOException, InterruptedException, TimeoutException {
        return testPlan(dslThreadGroup, jtlWriter("target", "test-" + id + ".jtl"))
                .runIn(dslJmeterEngine);
    }

    @ReadCache
    public DslDefaultThreadGroup getThreadGroup(final String id) {
        throw new IllegalArgumentException("No DslThreadGroup could be found in test local cache after id = " + id);
    }

    @WriteCache
    public DslDefaultThreadGroup createThreadGroup(final String id,
                                                   final int threads,
                                                   final int iterations) {
        return threadGroup(id, threads, iterations);
    }

    @ReadCache
    public DslHttpSampler getHttpSampler(final String id) {
        throw new IllegalArgumentException("No DslHttpSampler could be found in test local cache after id = " + id);
    }

    @WriteCache
    public DslHttpSampler createHttpSampler(final String id,
                                            final HttpRequestDetails httpRequestDetails) {
        final var dslHttpSampler = httpSampler(httpRequestDetails.getUrl()).method(httpRequestDetails.getMethod().toString());
        ofNullable(httpRequestDetails.getBody()).ifPresent(dslHttpSampler::body);
        ofNullable(httpRequestDetails.getHeaders()).orElse(Map.of()).forEach(dslHttpSampler::header);
        return dslHttpSampler;
    }

    @ReadCache
    public DslJmeterEngine getEngine(final String id) {
        throw new IllegalArgumentException("No DslJmeterEngine could be found in test local cache after id = " + id);
    }

    @WriteCache
    public DslJmeterEngine createBlazeMeterEngine(final String id,
                                                  final BlazeMeterDetails blazeMeterDetails) {
        return new BlazeMeterEngine(blazeMeterDetails.getToken())
                .testName(blazeMeterDetails.getTestName())
                .totalUsers(blazeMeterDetails.getTotalUsers())
                .threadsPerEngine(blazeMeterDetails.getThreadsPerEngine())
                .iterations(blazeMeterDetails.getIterations())
                .holdFor(Duration.ofSeconds(blazeMeterDetails.getHoldFor()))
                .testTimeout(Duration.ofSeconds(blazeMeterDetails.getTestTimeout()));
    }

    public void createRegexExtractor(final DslHttpSampler dslHttpSampler,
                                     final String name,
                                     final String value) {
        dslHttpSampler.children(regexExtractor(name, value));
    }

}
