import org.joda.time.DateTime;

interface TimeBucketKey {
    String getKey(DateTime d);
}