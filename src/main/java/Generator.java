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
        if (startTimeUtc.equals(endTimeUtc)) {
            return TimeKeyUnit.HOURS.getAllKeys(startTimeUtc);
        }
        if (endTimeUtc.minusYears(startTimeUtc.getYear()).getYear() >= 1) {
            keys.addYearKey(generateYearKeys(startTimeUtc, endTimeUtc));
        }
        return keys;
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
