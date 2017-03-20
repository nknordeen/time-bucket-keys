import org.joda.time.DateTime;

interface DateTimeFactory {
    DateTime construct(final DateTime reference, final int newTimeValue);
}