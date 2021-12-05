package ro.mihaisurdeanu.testing.framework.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpMethod;
import org.springframework.stereotype.Service;
import ro.mihaisurdeanu.testing.framework.aop.ReadCache;
import ro.mihaisurdeanu.testing.framework.aop.WriteCache;
import ro.mihaisurdeanu.testing.framework.model.HttpRequestDetails;
import us.abstracta.jmeter.javadsl.JmeterDsl;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.threadgroups.DslThreadGroup;
import us.abstracta.jmeter.javadsl.http.DslHttpSampler;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

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
    public TestPlanStats createAndScheduleTestPlan(final String id, final DslThreadGroup dslThreadGroup) throws IOException {
        return testPlan(dslThreadGroup, JmeterDsl.jtlWriter("./target/test-" + id + "-" + Instant.now().toString().replaceAll("[\\:|\\.]]", "-") + ".jtl"))
                .run();
    }

    @ReadCache
    public DslThreadGroup getThreadGroup(final String id) {
        throw new IllegalArgumentException("No DslThreadGroup could be found in test local cache after id = " + id);
    }

    @WriteCache
    public DslThreadGroup createThreadGroup(final String id, final int threads, final int iterations) {
        return threadGroup(id, threads, iterations);
    }

    @ReadCache
    public DslHttpSampler getHttpSampler(final String id) {
        throw new IllegalArgumentException("No DslHttpSampler could be found in test local cache after id = " + id);
    }

    @WriteCache
    public DslHttpSampler createHttpSampler(final String id, final HttpRequestDetails httpRequestDetails) {
        final var dslHttpSampler = httpSampler(httpRequestDetails.getUrl()).method(HttpMethod.valueOf(httpRequestDetails.getMethod().toString()));
        ofNullable(httpRequestDetails.getBody()).ifPresent(dslHttpSampler::body);
        ofNullable(httpRequestDetails.getHeaders()).orElse(Map.of()).forEach(dslHttpSampler::header);
        return dslHttpSampler;
    }

    public void createRegexExtractor(final DslHttpSampler dslHttpSampler, final String name, final String value) {
        dslHttpSampler.children(regexExtractor(name, value));
    }

}
