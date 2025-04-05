package org.isolution.chronotools;

import java.util.concurrent.TimeUnit;

public final class UTCTime {
    private static final int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private UTCTime() {
        // Prevent instantiation
    }

    /**
     * Assumes date string is in the format 'yyyyMMdd-HH:mm:ss.SSS'
     */
    public static long getEpochNanosFromCompactFormat(final String utcDateTimeString) {
        final short year = Short.parseShort(utcDateTimeString.substring(0, 4));
        final byte month = Byte.parseByte(utcDateTimeString.substring(4, 6)); // 1-based
        final byte day = Byte.parseByte(utcDateTimeString.substring(6, 8));
        final byte hour = Byte.parseByte(utcDateTimeString.substring(9, 11));
        final byte minute = Byte.parseByte(utcDateTimeString.substring(12, 14));
        final byte second = Byte.parseByte(utcDateTimeString.substring(15, 17));
        final short millis = Short.parseShort(utcDateTimeString.substring(18, 21));
        return computeEpochMillis(year, month, day, hour, minute, second, millis);
    }

    /**
     * Assumes date string is in the format {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME}
     */
    public static long getEpochNanosFromISOFormat(final String utcDateTimeString) {
        final short year = Short.parseShort(utcDateTimeString.substring(0, 4));
        final byte month = Byte.parseByte(utcDateTimeString.substring(5, 7)); // 1-based
        final byte day = Byte.parseByte(utcDateTimeString.substring(8, 10));
        final byte hour = Byte.parseByte(utcDateTimeString.substring(11, 13));
        final byte minute = Byte.parseByte(utcDateTimeString.substring(14, 16));
        final byte second = Byte.parseByte(utcDateTimeString.substring(17, 19));
        final short millis = Short.parseShort(utcDateTimeString.substring(20, 23));
        return computeEpochMillis(year, month, day, hour, minute, second, millis);
    }


    public static String convertEpochMillisToISOFormat(final long epochMillis) {
        long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(epochMillis);
        long millis = epochMillis % 1000;

        long days = totalSeconds / 86400;
        long remainingSeconds = totalSeconds % 86400;

        int hour = (int) (remainingSeconds / 3600);
        remainingSeconds %= 3600;
        int minute = (int) (remainingSeconds / 60);
        int second = (int) (remainingSeconds % 60);

        short year = 1970;
        while (days >= (isLeapYear(year) ? 366 : 365)) {
            days -= isLeapYear(year) ? 366 : 365;
            year++;
        }

        int month = 0;
        while (days >= (daysInMonth[month] + (month == 1 && isLeapYear(year) ? 1 : 0))) {
            days -= daysInMonth[month] + (month == 1 && isLeapYear(year) ? 1 : 0);
            month++;
        }

        int day = (int) days + 1;

        return String.format("%04d-%02d-%02dT%02d:%02d:%02d.%03d", year, month + 1, day, hour, minute, second, millis);
    }


    private static long computeEpochMillis(short year, byte month, byte day,
                                           byte hour, byte minute, byte second,
                                           short millis) {
        long days = 0;

        // Add days for each year
        for (short y = 1970; y < year; y++) {
            days += isLeapYear(y) ? 366 : 365;
        }

        // Add days for each month in the current year
        for (byte m = 1; m < month; m++) {
            if (m == 2 && isLeapYear(year)) {
                days += 29;
            } else {
                days += daysInMonth[m - 1];
            }
        }

        // Add days in current month
        days += day - 1;

        // Total seconds
        long totalSeconds = days * 86_400L + hour * 3_600L + minute * 60L + second;

        // Return milliseconds
        long totalMillis = (totalSeconds * 1000L) + millis;

        return TimeUnit.NANOSECONDS.convert(totalMillis, TimeUnit.MILLISECONDS); // Convert to nanoseconds
    }

    private static boolean isLeapYear(short year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }
}
