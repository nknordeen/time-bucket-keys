import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class TimeKeyUnitTest {

    @Test
    public void fourDigitYear() {
        DateTime date = new DateTime(1970, 11, 5, 13, 50);
        Assert.assertEquals("1970", TimeKeyUnit.YEARS.getKey(date));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidYear() {
        TimeKeyUnit.YEARS.getKey(new DateTime(1969, 12, 31, 13, 50));
    }

    @Test
    public void twoDigitMonth() {
        DateTime date = new DateTime(1970, 11, 5, 13, 50);
        Assert.assertEquals("197011", TimeKeyUnit.MONTHS.getKey(date));
    }

    @Test
    public void oneDigitMonth() {
        DateTime date = new DateTime(1970, 9, 5, 13, 50);
        Assert.assertEquals("197009", TimeKeyUnit.MONTHS.getKey(date));
    }

    @Test
    public void twoDigitDay() {
        DateTime date = new DateTime(1970, 11, 10, 13, 50);
        Assert.assertEquals("19701110", TimeKeyUnit.DAYS.getKey(date));
    }

    @Test
    public void oneDigitDay() {
        DateTime date = new DateTime(1970, 9, 5, 13, 50);
        Assert.assertEquals("19700905", TimeKeyUnit.DAYS.getKey(date));
    }
    @Test
    public void twoDigitHour() {
        DateTime date = new DateTime(1970, 11, 5, 13, 50);
        Assert.assertEquals("1970110513", TimeKeyUnit.HOURS.getKey(date));
    }

    @Test
    public void oneDigitHour() {
        DateTime date = new DateTime(1970, 9, 5, 9, 50);
        Assert.assertEquals("1970090509", TimeKeyUnit.HOURS.getKey(date));
    }

}
