import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

public class Generator {
    private Generator(){}

    public AllKeys generateKeys(DateTime startTime, DateTime endTime, String timeZone) {
        return new AllKeys();
    }

    public AllKeys generateKeys(DateTime time, TimeUnit timeUnit, String timeZone) {
        return new AllKeys();
    }
}
