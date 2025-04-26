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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
public class MapImplBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testHashMapWithoutCapacity(Blackhole blackhole) {
        final var map = new HashMap<String, Object>();
        map.put("key1", "value");
        map.put("key2", 1);
        map.put("key3", true);
        blackhole.consume(map);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testHashMapWithCapacity(Blackhole blackhole) {
        final var map = new HashMap<String, Object>(3);
        map.put("key1", "value");
        map.put("key2", 1);
        map.put("key3", true);
        blackhole.consume(map);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void testEnumMap(Blackhole blackhole) {
        final var map = new EnumMap<>(KeyEnum.class);
        map.put(KeyEnum.KEY1, "value");
        map.put(KeyEnum.KEY2, 1);
        map.put(KeyEnum.KEY3, true);
        blackhole.consume(map);
    }

    public enum KeyEnum {
        KEY1, KEY2, KEY3
    }

    public static void main(String[] args) throws RunnerException {
        final var options = new OptionsBuilder()
                .include(MapImplBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .build();

        new Runner(options).run();
    }

}
