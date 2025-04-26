package ro.mihaisurdeanu.testing.framework.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
public class StringConcatBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testClassicalApproach(Blackhole blackhole) {
        String value = "";
        for (int i = 0; i < 1000; i++) {
            value += i;
        }
        blackhole.consume(value);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testBuilderApproach(Blackhole blackhole) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            value.append(i);
        }
        blackhole.consume(value.toString());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testBufferApproach(Blackhole blackhole) {
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < 1000; i++) {
            value.append(i);
        }
        blackhole.consume(value.toString());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testStreamApproach(Blackhole blackhole) {
        IntStream.range(0, 1000)
                .boxed()
                .map(String::valueOf)
                .reduce((a, b) -> a + b)
                .ifPresent(blackhole::consume);
    }

    public static void main(String[] args) throws RunnerException {
        final var options = new OptionsBuilder()
                .include(StringConcatBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .build();

        new Runner(options).run();
    }

}
