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

    @Test
    public void isEndOfYear() {
        DateTime date = new DateTime(2016, 12, 31, 23, 0);
        Assert.assertTrue(TimeKeyUnit.YEARS.isEnd(date));
    }

    @Test
    public void notEndOfYear() {
        Assert.assertTrue(TimeKeyUnit.YEARS.isEnd(new DateTime(2016, 12, 31, 23, 0)));
        Assert.assertFalse(TimeKeyUnit.YEARS.isEnd(new DateTime(2016, 12, 30, 23, 0)));
    }

    @Test
    public void isEndOfLeapMonth() {
        DateTime date = new DateTime(2016, 2, 29, 23, 0);
        Assert.assertTrue(TimeKeyUnit.MONTHS.isEnd(date));
    }

    @Test
    public void notEndOfMonth() {
        DateTime date = new DateTime(2016, 2, 20, 23, 0);
        Assert.assertFalse(TimeKeyUnit.MONTHS.isEnd(date));
    }

    @Test
    public void isEndOfDay() {
        DateTime date = new DateTime(2016, 2, 2, 23, 0);
        Assert.assertTrue(TimeKeyUnit.DAYS.isEnd(date));
    }

    @Test
    public void notEndOfDay() {
        DateTime date = new DateTime(2016, 2, 2, 2, 0);
        Assert.assertFalse(TimeKeyUnit.DAYS.isEnd(date));
    }
}
