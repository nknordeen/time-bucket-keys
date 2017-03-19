import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.*;

public class Generator {
    
    private Generator(){}

    public static AllKeys generateKeys(DateTime startTime, DateTime endTime, String timeZone) {
        return new AllKeys();
    }

    /**
     * data stored in db are keyed by time buckets ex: YYYY and YYYYMM and YYYYMMdd and YYYYMMddHH
     * and in UTC
     * @param time - the DateTime used to generate the single key
     * @param timeUnit - Will specify the size of they time bucket
     * @param timeZone - time zone of `time`.
     * @return - `AllKeys` the keys used to query the database
     */
    public static AllKeys generateKeys(DateTime time, TimeKeyUnit timeUnit, String timeZone) {
        final DateTime utcWithTimeZone = new DateTime(time, DateTimeZone.forID(timeZone)).withZone(DateTimeZone.UTC);//time.withZone(DateTimeZone.UTC).withZone(DateTimeZone.forID(timeZone));
        return timeUnit.getAllKeys(utcWithTimeZone);
    }
}
