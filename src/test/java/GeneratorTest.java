import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class GeneratorTest {

    final String TIME_ZONE_VALID = "America/Phoenix";
    final String TIME_ZONE_INVALID = "invalidTimeZone";
    @Test
    public void with_validTimeAndTimeZone_shouldPass() {
        DateTime startTime = new DateTime(2017, 9, 5, 9, 50);
        AllKeys keys = Generator.generateKeys(startTime, TimeKeyUnit.DAYS, TIME_ZONE_VALID);
        Assert.assertEquals(keys.getHourKeys(), Collections.<String>emptySet());
        Assert.assertEquals(keys.getDayKeys().size(), 1);
        Assert.assertTrue(keys.getDayKeys().contains("20170905"));
        Assert.assertEquals(keys.getMonthKeys(), Collections.<String>emptySet());
        Assert.assertEquals(keys.getYearKeys(), Collections.<String>emptySet());
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
}
