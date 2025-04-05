package org.isolution.chronotools;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.isolution.chronotools.UTCTime.getEpochNanosFromCompactFormat;

public class UTCTimeJmhTest {


    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test_using_library(final Blackhole blackhole) {
        long epochNanosFromISOFormat = getEpochNanosFromCompactFormat("20230315-12:34:56.789");
        blackhole.consume(epochNanosFromISOFormat);
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test_using_jdk(final Blackhole blackhole) {
        final LocalDateTime parse = LocalDateTime.parse("20230315-12:34:56.789", FORMATTER);
        final long epochSecond = parse.toEpochSecond(ZoneOffset.UTC);
        final int nanoAdjustment = parse.getNano();
        blackhole.consume(epochSecond * 1_000_000_000L + nanoAdjustment);
    }
}
