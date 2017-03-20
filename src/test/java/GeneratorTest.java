import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneratorTest {

    final String TIME_ZONE_VALID = "America/Phoenix";
    final String TIME_ZONE_INVALID = "invalidTimeZone";

    @Test
    public void with_validTimeAndTimeZone_shouldPass() {
        DateTime startTime = new DateTime(2017, 9, 5, 9, 50);
        AllKeys result = Generator.generateKeys(startTime, TimeKeyUnit.DAYS, TIME_ZONE_VALID);
        Assert.assertEquals(result.getHourKeys(), Collections.<String>emptySet());
        Assert.assertEquals(result.getDayKeys().size(), 1);
        Assert.assertTrue(result.getDayKeys().contains("20170905"));
        Assert.assertEquals(result.getMonthKeys(), Collections.<String>emptySet());
        Assert.assertEquals(result.getYearKeys(), Collections.<String>emptySet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void with_invalidTimeZone_shouldFail() {
        DateTime startTime = new DateTime(2017, 9, 5, 9, 50);
        Generator.generateKeys(startTime, TimeKeyUnit.DAYS, TIME_ZONE_INVALID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void with_timeBeforeEpoch_shouldFail() {
        DateTime startTime = new DateTime(1969, 9, 5, 9, 50);
        Generator.generateKeys(startTime, TimeKeyUnit.DAYS, TIME_ZONE_VALID);
    }

    @Test
    public void withSameTimeStamps_shouldReturnOneDayKey() {
        DateTime startTime = new DateTime(2017, 1, 5, 9, 50, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2017, 1, 5, 9, 50, DateTimeZone.UTC);
        AllKeys result = Generator.generateKeys(startTime, endTime, TIME_ZONE_VALID);
        Assert.assertEquals(result.getHourKeys().size(), 1);
        Assert.assertTrue(result.getHourKeys().contains("2017010509"));
        Assert.assertEquals(result.getDayKeys(), Collections.<String>emptySet());
        Assert.assertEquals(result.getMonthKeys(), Collections.<String>emptySet());
        Assert.assertEquals(result.getYearKeys(), Collections.<String>emptySet());
    }

    @Test
    public void withStartAtEndOfYearAndEndAtStartOfYear_withTwoYearsApart_shouldAddMiddleYear() {
        DateTime startTime = new DateTime(2015, 12, 2, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2017, 1, 5, 9, 50, DateTimeZone.UTC);
        AllKeys result = Generator.generateKeys(startTime, endTime, TIME_ZONE_VALID);
        Assert.assertEquals(result.getYearKeys().size(), 1);
        Assert.assertTrue(result.getYearKeys().contains("2016"));
    }
    @Test
    public void withStartGreaterThanEnd_shouldStillFindYearKey() {
        DateTime startTime = new DateTime(2015, 12, 2, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2017, 1, 5, 9, 50, DateTimeZone.UTC);
        AllKeys result = Generator.generateKeys(endTime, startTime, TIME_ZONE_VALID);
        Assert.assertEquals(result.getYearKeys().size(), 1);
        Assert.assertTrue(result.getYearKeys().contains("2016"));
    }

    @Test
    public void withStartAtStartOfYearAndEndAtStartOfYear_withTwoYearsApart_shouldAddTwoYears() {
        DateTime startTime = new DateTime(2015, 1, 1, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2017, 1, 5, 9, 50, DateTimeZone.UTC);
        AllKeys result = Generator.generateKeys(startTime, endTime, TIME_ZONE_VALID);
        Assert.assertEquals(result.getYearKeys().size(), 2);
        Assert.assertTrue(result.getYearKeys().contains("2015"));
        Assert.assertTrue(result.getYearKeys().contains("2016"));
    }
}
