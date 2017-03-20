import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {

    public static AllKeys generateKeys(final DateTime startTime, final DateTime endTime, final String timeZone) {
        final DateTime startTimeUtc;
        final DateTime endTimeUtc;
        if (startTime == null) {
            throw new IllegalArgumentException("StartTime is required to generate the query keys");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("EndTime is required to generate the query keys");
        }

        final DateTimeZone defaultedTimeZone;
        if (timeZone == null) {
            System.out.println("TimeZone was null, defaulting to UTC");
            defaultedTimeZone = DateTimeZone.UTC;
        } else {
            defaultedTimeZone = DateTimeZone.forID(timeZone);
        }
        // No need to error out if the start and end are switched, as long as we have the range then we can switch ourselves.
        if (endTime.isBefore(startTime)) {
            startTimeUtc = new DateTime(endTime, defaultedTimeZone).withZone(DateTimeZone.UTC);
            endTimeUtc = new DateTime(startTime, defaultedTimeZone).withZone(DateTimeZone.UTC);
        } else {
            startTimeUtc = new DateTime(startTime, defaultedTimeZone).withZone(DateTimeZone.UTC);
            endTimeUtc = new DateTime(endTime, defaultedTimeZone).withZone(DateTimeZone.UTC);
        }
        AllKeys keys = new AllKeys();
        int totalYears = endTimeUtc.minusYears(startTimeUtc.getYear()).getYear();

        final DateTimeFactory dateTimeMonthConstructor = (reference, month) -> new DateTime(reference.getYear(), month, 1, 0, 0, DateTimeZone.UTC);
        final DateTimeFactory dateTimeYearConstructor = (reference, year) -> new DateTime(year, reference.getMonthOfYear(), 1, 0, 0, DateTimeZone.UTC);

        if (startTimeUtc.equals(endTimeUtc)) {
            return TimeKeyUnit.HOURS.getAllKeys(startTimeUtc);
        }
        if (totalYears >= 1) {
            int period = yearTimePeriod(startTimeUtc, endTimeUtc);
            keys.addYearKeys(generateKeysForTimeUnit(startTimeUtc, endTimeUtc, TimeKeyUnit.YEARS, period,
                    dateTimeYearConstructor, startTimeUtc.getYear(), endTimeUtc.getYear()));
        }
        if (totalYears == 0) {
            int period = monthTimePeriod(startTimeUtc, endTimeUtc);
            keys.addMonthKeys(generateKeysForTimeUnit(startTimeUtc, endTimeUtc, TimeKeyUnit.MONTHS, period,
                    dateTimeMonthConstructor, startTimeUtc.getMonthOfYear(), endTimeUtc.getMonthOfYear()));
        } else {
            final DateTime beginningOfYear = beginningOfYear(endTimeUtc.getYear());
            final DateTime endOfYear = endOfYear(startTimeUtc.getYear());

            final int startPeriod = monthTimePeriod(startTimeUtc, endOfYear);
            keys.addMonthKeys(generateKeysForTimeUnit(startTimeUtc, endOfYear, TimeKeyUnit.MONTHS, startPeriod,
                    dateTimeMonthConstructor, startTimeUtc.getMonthOfYear(), endOfYear.getMonthOfYear()));

            final int endPeriod = monthTimePeriod(beginningOfYear, endTimeUtc);
            keys.addMonthKeys(generateKeysForTimeUnit(beginningOfYear, endTimeUtc, TimeKeyUnit.MONTHS, endPeriod,
                    dateTimeMonthConstructor, beginningOfYear.getMonthOfYear(), endTimeUtc.getMonthOfYear()));
        }
        return keys;
    }

    private static DateTime beginningOfYear(final int year) {
        return new DateTime(year, 1, 1, 0, 0, DateTimeZone.UTC);
    }

    private static DateTime endOfYear(final int year) {
        return new DateTime(year, 12, 31, 23, 59, DateTimeZone.UTC);
    }

    private static int monthTimePeriod(final DateTime startTime, final DateTime endTime) {
        if (TimeKeyUnit.MONTHS.isEnd(endTime)) {
            return endTime.plusHours(1).minusMonths(startTime.getMonthOfYear()).getMonthOfYear();
        } else {
            return endTime.minusMonths(startTime.getMonthOfYear()).getMonthOfYear();
        }
    }

    private static int yearTimePeriod(final DateTime startTime, final DateTime endTime) {
        if (TimeKeyUnit.YEARS.isEnd(endTime)) {
            return endTime.plusHours(1).minusYears(startTime.getYear()).getYear();
        } else {
            return endTime.minusMonths(startTime.getYear()).getYear();
        }
    }

    private static List<String> generateKeysForTimeUnit(final DateTime startTime, final DateTime endTime,
                                                        final TimeKeyUnit timeUnit, final int totalTimePeriod,
                                                        final DateTimeFactory factory, final int startOfRange, final int endOfRange) {

        if (totalTimePeriod > 1 && timeUnit.isBeginning(startTime) && !timeUnit.isEnd(endTime)) {
            return IntStream.range(startOfRange, endOfRange)
                    .boxed()
                    .map(unit -> timeUnit.getKey(factory.construct(startTime, unit)))
                    .collect(Collectors.toList());
        } else if (totalTimePeriod > 1 && timeUnit.isBeginning(startTime) && timeUnit.isEnd(endTime)) {
            return IntStream.rangeClosed(startOfRange, endOfRange)
                    .boxed()
                    .map(unit -> timeUnit.getKey(factory.construct(startTime, unit)))
                    .collect(Collectors.toList());
        } else if (totalTimePeriod > 1 && !timeUnit.isBeginning(startTime) && timeUnit.isEnd(endTime)) {
            return IntStream.rangeClosed(startOfRange + 1, endOfRange)
                    .boxed()
                    .map(unit -> timeUnit.getKey(factory.construct(startTime, unit)))
                    .collect(Collectors.toList());
        } else if (totalTimePeriod > 1 && !timeUnit.isBeginning(startTime)) {
            return IntStream.range(startOfRange + 1, endOfRange)
                    .boxed()
                    .map(unit -> timeUnit.getKey(factory.construct(startTime, unit)))
                    .collect(Collectors.toList());
        } else if (totalTimePeriod == 1 && timeUnit.isBeginning(startTime)) {
            return Collections.singletonList(timeUnit.getKey(startTime));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * data stored in db are keyed by time buckets ex: YYYY and YYYYMM and YYYYMMdd and YYYYMMddHH
     * and in UTC
     * @param time - the DateTime used to generate the single key
     * @param timeUnit - Will specify the size of they time bucket
     * @param timeZone - time zone of `time`.  If null is passed in we default to UTC
     * @return - `AllKeys` the keys used to query the database
     */
    public static AllKeys generateKeys(final DateTime time, final TimeKeyUnit timeUnit, final String timeZone) {
        if (time == null) {
            throw new IllegalArgumentException("Time is required to generate keys for the query");
        }
        if (timeZone == null) {
            System.out.println("TimeZone was null, defaulting to UTC");
            final DateTime utcWithTimeZone = new DateTime(time, DateTimeZone.forID("UTC")).withZone(DateTimeZone.UTC);
            return timeUnit.getAllKeys(utcWithTimeZone);
        }
        final DateTime utcWithTimeZone = new DateTime(time, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);
        return timeUnit.getAllKeys(utcWithTimeZone);
    }
}
