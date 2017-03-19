import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum TimeKeyUnit implements TimeBucketKey {
    YEARS {
        public String getKey(DateTime d) {
            // joda time allows time before 1970, but negative time seems like someone might
            // be misusing this system.
            if (d.getMillis() < 0) {
                throw new IllegalArgumentException("Invalid time before epoch");
            }
            return Integer.toString(d.getYear());
        }

        public AllKeys getAllKeys(DateTime d) {
            return new AllKeys(Collections.<String>emptySet(),
                    Collections.<String>emptySet(),
                    Collections.<String>emptySet(),
                    new HashSet<String>(Collections.singletonList(getKey(d)))
            );
        }
    },
    MONTHS {
        public String getKey(DateTime d) {
            String year = YEARS.getKey(d);
            StringBuilder s = new StringBuilder(6);
            if (d.getMonthOfYear() < 10) {
                return s.append(year).append("0").append(d.getMonthOfYear()).toString();
            } else {
                return s.append(year).append(d.getMonthOfYear()).toString();
            }
        }

        public AllKeys getAllKeys(DateTime d) {
            return new AllKeys(Collections.<String>emptySet(),
                    Collections.<String>emptySet(),
                    new HashSet<String>(Collections.singletonList(getKey(d))),
                    Collections.<String>emptySet()
            );
        }
    },
    DAYS {
        public String getKey(DateTime d) {
            String month = MONTHS.getKey(d);
            StringBuilder s = new StringBuilder(8);
            if (d.getDayOfMonth() < 10) {
                return s.append(month).append("0").append(d.getDayOfMonth()).toString();
            } else {
                return s.append(month).append(d.getDayOfMonth()).toString();
            }
        }

        public AllKeys getAllKeys(DateTime d) {
            return new AllKeys(Collections.<String>emptySet(),
                    new HashSet<String>(Collections.singletonList(getKey(d))),
                    Collections.<String>emptySet(),
                    Collections.<String>emptySet()
            );
        }
    },
    HOURS {
        public String getKey(DateTime d) {
            String day = DAYS.getKey(d);
            StringBuilder s = new StringBuilder(8);
            if (d.getHourOfDay() < 10) {
                return s.append(day).append("0").append(d.getHourOfDay()).toString();
            } else {
                return s.append(day).append(d.getHourOfDay()).toString();
            }
        }

        public AllKeys getAllKeys(DateTime d) {
            return new AllKeys(new HashSet<String>(Collections.singletonList(getKey(d))),
                    Collections.<String>emptySet(),
                    Collections.<String>emptySet(),
                    Collections.<String>emptySet()
            );
        }
    }
}
