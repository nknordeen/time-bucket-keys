import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    
    private Generator(){}

    public static AllKeys generateKeys(final DateTime startTime, final DateTime endTime, final String timeZone) {
        final DateTime startTimeUtc;
        final DateTime endTimeUtc;
        if (endTime.isBefore(startTime)) {
            startTimeUtc = new DateTime(endTime, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
            endTimeUtc = new DateTime(startTime, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
        } else {
            startTimeUtc = new DateTime(startTime, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
            endTimeUtc = new DateTime(endTime, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
        }
        AllKeys keys = new AllKeys();
        int totalYears = endTimeUtc.minusYears(startTimeUtc.getYear()).getYear();
        if (startTimeUtc.equals(endTimeUtc)) {
            return TimeKeyUnit.HOURS.getAllKeys(startTimeUtc);
        }
        if (totalYears >= 1) {
            keys.addYearKeys(generateYearKeys(startTimeUtc, endTimeUtc));
        }
        if (totalYears == 0) {
            keys.addMonthKeys(generateMonthKeys(startTimeUtc, endTimeUtc, timeZone));
        } else {
            keys.addMonthKeys(generateMonthKeys(startTimeUtc,
                    endOfYear(startTimeUtc.getYear(), timeZone), timeZone));
            keys.addMonthKeys(generateMonthKeys(beginningOfYear(endTimeUtc.getYear(), timeZone),
                    endTimeUtc, timeZone));
        }
        return keys;
    }

    private static DateTime beginningOfYear(final int year, final String timeZone) {
        return new DateTime(year, 1, 1, 0, 0, DateTimeZone.UTC);
    }

    private static DateTime endOfYear(final int year, final String timeZone) {
        return new DateTime(year, 12, 31, 23, 59, DateTimeZone.UTC);
    }

    private static List<String> generateYearKeys(final DateTime startTime, final DateTime endTime) {
        int totalYears = endTime.minusYears(startTime.getYear()).getYear();
        if (totalYears > 1 && TimeKeyUnit.YEARS.isBeginning(startTime)) {
            return IntStream.range(startTime.getYear(), endTime.getYear())
                    .boxed()
                    .map(year -> Integer.toString(year))
                    .collect(Collectors.toList());
        } else if (totalYears > 1 && !TimeKeyUnit.YEARS.isBeginning(startTime)) {
            return IntStream.range(startTime.getYear() + 1, endTime.getYear())
                    .boxed()
                    .map(year -> Integer.toString(year))
                    .collect(Collectors.toList());
        } else if (totalYears == 1 && TimeKeyUnit.YEARS.isBeginning(startTime)) {
            return Arrays.asList(TimeKeyUnit.YEARS.getKey(startTime));
        } else {
            return Collections.emptyList();
        }
    }

    private static List<String> generateMonthKeys(final DateTime startTime, final DateTime endTime, final String timeZone) {
        int totalMonths;
        if (TimeKeyUnit.MONTHS.isEnd(endTime)) {
            totalMonths = endTime.plusHours(1).minusMonths(startTime.getMonthOfYear()).getMonthOfYear();
        } else {
            totalMonths = endTime.minusMonths(startTime.getMonthOfYear()).getMonthOfYear();
        }

        if (totalMonths > 1 && TimeKeyUnit.MONTHS.isBeginning(startTime)) {
            return IntStream.rangeClosed(startTime.getMonthOfYear(), endTime.getMonthOfYear())
                    .boxed()
                    .map(month -> TimeKeyUnit.MONTHS.getKey(monthToDate(startTime.getYear(), month, timeZone)))
                    .collect(Collectors.toList());
        } else if (totalMonths > 1 && !TimeKeyUnit.MONTHS.isBeginning(startTime)) {
            return IntStream.rangeClosed(startTime.getMonthOfYear() + 1, endTime.getMonthOfYear())
                    .boxed()
                    .map(month -> TimeKeyUnit.MONTHS.getKey(monthToDate(startTime.getYear(), month, timeZone)))
                    .collect(Collectors.toList());
        } else if (totalMonths == 1 && TimeKeyUnit.MONTHS.isBeginning(startTime)) {
            return Arrays.asList(TimeKeyUnit.MONTHS.getKey(startTime));
        } else {
            return Collections.emptyList();
        }
    }

    private static DateTime monthToDate(final int year, final int month, final String timeZone) {
        return new DateTime(year, month, 1, 0, 0, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
    }

    /**
     * data stored in db are keyed by time buckets ex: YYYY and YYYYMM and YYYYMMdd and YYYYMMddHH
     * and in UTC
     * @param time - the DateTime used to generate the single key
     * @param timeUnit - Will specify the size of they time bucket
     * @param timeZone - time zone of `time`.
     * @return - `AllKeys` the keys used to query the database
     */
    public static AllKeys generateKeys(final DateTime time, final TimeKeyUnit timeUnit, final String timeZone) {
        final DateTime utcWithTimeZone = new DateTime(time, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
        return timeUnit.getAllKeys(utcWithTimeZone);
    }
}
