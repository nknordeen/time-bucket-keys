import org.joda.time.DateTime;

interface TimeBucketKey {
    boolean isBeginning(DateTime d);
    boolean isEnd(DateTime d);
    String getKey(DateTime d);
    AllKeys getAllKeys(DateTime d);
}