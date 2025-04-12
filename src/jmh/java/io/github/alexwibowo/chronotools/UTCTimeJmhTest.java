package io.github.alexwibowo.chronotools;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static io.github.alexwibowo.chronotools.UTCTime.getEpochNanosFromCompactFormat;

public class UTCTimeJmhTest {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void chronotools_benchmark(final Blackhole blackhole) {
        long epochNanosFromISOFormat = getEpochNanosFromCompactFormat("20230315-12:34:56.789");
        blackhole.consume(epochNanosFromISOFormat);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void jdk_benchmark(final Blackhole blackhole) {
        final LocalDateTime parse = LocalDateTime.parse("20230315-12:34:56.789", FORMATTER);
        final long epochSecond = parse.toEpochSecond(ZoneOffset.UTC);
        final int nanoAdjustment = parse.getNano();
        blackhole.consume(epochSecond * 1_000_000_000L + nanoAdjustment);
    }
}
