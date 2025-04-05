package org.isolution.chronotools;

import org.bw.ByteWatcher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.isolution.chronotools.UTCTime.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UTCTimeTest {

    @Test
    void daylightSavingsEnd() {
        // April 6, 2025, 2:00:00 am AEDT.
        // April 6, 2025, 4:00:00 pm UTC
        // 1743955200000
        long expectedNanos = 1743955200000_000_000L;
        assertThat(getEpochNanosFromISOFormat("2025-04-06T16:00:00.000"))
                .isEqualTo(expectedNanos);
        assertThat(getEpochNanosFromCompactFormat("20250406-16:00:00.000"))
                .isEqualTo(expectedNanos);
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(expectedNanos, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T16:00:00.000");

        // April 7, 2025, 3:00:00 am AEDT.
        // April 6, 2025, 5:00:00 pm UTC
        // 1743958800000
        long expectedNanos2 = 1743958800000_000_000L;
        assertThat(getEpochNanosFromISOFormat("2025-04-06T17:00:00.000"))
                .isEqualTo(expectedNanos2);
        assertThat(getEpochNanosFromCompactFormat("20250406-17:00:00.000"))
                .isEqualTo(expectedNanos2);
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(expectedNanos2, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T17:00:00.000");


        // April 7, 2025, 3:00:01 am AEDT.
        // April 6, 2025, 5:00:01 pm UTC
        // 1743958801000
        long expectedNanos3 = 1743958801000_000_000L;
        assertThat(getEpochNanosFromISOFormat("2025-04-06T17:00:01.000"))
                .isEqualTo(expectedNanos3);
        assertThat(getEpochNanosFromCompactFormat("20250406-17:00:01.000"))
                .isEqualTo(expectedNanos3);
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(expectedNanos3, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T17:00:01.000");


        // April 7, 2025, 4:00:01 am AEDT.
        // April 6, 2025, 6:00:01 pm UTC
        // 1743962401000
        long expectedNanos4 = 1743962401000_000_000L;
        assertThat(getEpochNanosFromISOFormat("2025-04-06T18:00:01.000"))
                .isEqualTo(expectedNanos4);
        assertThat(getEpochNanosFromCompactFormat("20250406-18:00:01.000"))
                .isEqualTo(expectedNanos4);
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(expectedNanos4, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T18:00:01.000");
    }

    @Test
    void convertingToISOFormat() {
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(1743962400775_000_000L, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T18:00:00.775");
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(1743962400775_775_000L, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T18:00:00.775");
        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(1743962400775_775_775L, TimeUnit.NANOSECONDS)))
                .isEqualTo("2025-04-06T18:00:00.775");
    }

    @Test
    void daylightSavingStart() {
        // October 5, 2025  1:59:59 am AEDT
        // October 4, 2025, 3:59:59 pm UTC
        // 1759593599000
        long expectedNanos = 1759593599000_000_000L;
        assertThat(getEpochNanosFromISOFormat("2025-10-04T15:59:59.000"))
                .isEqualTo(expectedNanos);
        assertThat(getEpochNanosFromCompactFormat("20251004-15:59:59.000"))
                .isEqualTo(expectedNanos);

        // October 5, 2025 2:00:00 am AEDT // doesnt exist

        // October 5, 2025, 3:00:00 am AEDT
        // October 4, 2025, 4:00:00 pm UTC
        // 1759593600000
        long expectedNanos2 = 1759593600000_000_000L;
        assertThat(getEpochNanosFromISOFormat("2025-10-04T16:00:00.000"))
                .isEqualTo(expectedNanos2);
        assertThat(getEpochNanosFromCompactFormat("20251004-16:00:00.000"))
                .isEqualTo(expectedNanos2);
    }

    @Test
    void epochNanos_validDateTimeString_returnsCorrectNanos() {
        final long expectedNanos = 1678883696789000000L;
        assertThat(getEpochNanosFromISOFormat("2023-03-15T12:34:56.789"))
                .isEqualTo(expectedNanos);
        assertThat(getEpochNanosFromCompactFormat("20230315-12:34:56.789"))
                .isEqualTo(expectedNanos);

        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(expectedNanos, TimeUnit.NANOSECONDS)))
                .isEqualTo("2023-03-15T12:34:56.789");
    }

    @Test
    void getEpochNanos_should_not_do_any_allocation() throws Exception {
        long limit = 80<<20;
        ByteWatcher am = new ByteWatcher();
        am.onByteWatch((t, size) ->
                        System.out.printf("%s exceeded limit: %d using: %d%n",
                                t.getName(), limit, size)
                , limit);
        am.printAllAllocations();
        Thread t = new Thread("GarbageFreeThread") {
            public void run() {
                getEpochNanosFromISOFormat("2023-03-15T12:34:56.789");
                getEpochNanosFromCompactFormat("20230315-12:34:56.789");
            }
        };
        t.start();
        Thread.sleep(10000);
        am.printAllAllocations();
        assertThat(am.findThread("GarbageFreeThread")).isNotPresent();
    }

    @Test
    void epochNanos_leapYearDateTimeString_returnsCorrectNanos() {
        final long expectedNanos = 1582934400000000000L;
        assertThat(getEpochNanosFromISOFormat("2020-02-29T00:00:00.000"))
                .isEqualTo(expectedNanos);
        assertThat(getEpochNanosFromCompactFormat("20200229-00:00:00.000"))
                .isEqualTo(expectedNanos);

        assertThat(convertEpochMillisToISOFormat(TimeUnit.MILLISECONDS.convert(expectedNanos, TimeUnit.NANOSECONDS)))
                .isEqualTo("2020-02-29T00:00:00.000");
    }

    @Test
    void epochNanos_endOfYearDateTimeString_returnsCorrectNanos() {
        final long expectedNanos = 1672531199999000000L;
        assertThat(getEpochNanosFromISOFormat("2022-12-31T23:59:59.999"))
                .isEqualTo(expectedNanos);
        assertThat(getEpochNanosFromCompactFormat("20221231-23:59:59.999"))
                .isEqualTo(expectedNanos);
    }

    @Test
    void epochNanos_invalidDateTimeString_throwsException() {
        String dateTimeString = "invalid-date-time";
        assertThrows(NumberFormatException.class, () -> getEpochNanosFromISOFormat(dateTimeString));
    }

    @Test
    void epochNanos_emptyDateTimeString_throwsException() {
        String dateTimeString = "";
        assertThrows(StringIndexOutOfBoundsException.class, () -> getEpochNanosFromISOFormat(dateTimeString));
    }

    @Test
    void equality_test() {
        final long epochNanosFromISOFormat = getEpochNanosFromCompactFormat("20230315-12:34:56.789");

        final LocalDateTime parsed = LocalDateTime.parse("20230315-12:34:56.789", DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS"));
        final long result = parsed.toEpochSecond(ZoneOffset.UTC) * 1_000_000_000L + parsed.getNano();

        assertThat(epochNanosFromISOFormat)
                .isEqualTo(result);
    }
}
