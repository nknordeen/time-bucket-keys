import org.joda.time.DateTime;

public class Generator {
    private Generator(){}

    public static AllKeys generateKeys(DateTime startTime, DateTime endTime, String timeZone) {
        return new AllKeys();
    }

    public static AllKeys generateKeys(DateTime time, TimeKeyUnit timeUnit, String timeZone) {
        return new AllKeys();
    }
}
