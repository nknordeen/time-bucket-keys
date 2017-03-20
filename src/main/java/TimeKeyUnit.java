import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashSet;

public enum TimeKeyUnit implements TimeBucketKey {
    YEARS {
        public boolean isBeginning(DateTime d) {
            return d.getDayOfYear() == 1;
        }

        public boolean isEnd(DateTime d) {
            return d.dayOfYear().get() == d.dayOfYear().getMaximumValue() && MONTHS.isEnd(d);
        }

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
        public boolean isBeginning(DateTime d) {
            return d.getDayOfMonth() == 1;
        }

        public boolean isEnd(DateTime d) {
            return d.dayOfMonth().get() == d.dayOfMonth().getMaximumValue() && DAYS.isEnd(d);
        }

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
        public boolean isBeginning(DateTime d) {
            return d.getHourOfDay() == 0;
        }

        public boolean isEnd(DateTime d) {
            return d.getHourOfDay() == 23;
        }

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
        // Hour is the lowest measurement, so if any extra minutes or reference to this hour
        // we'll give it to them.
        public boolean isBeginning(DateTime d) {
            return true;
        }

        public boolean isEnd(DateTime d) {
            return true;
        }

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
