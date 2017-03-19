import org.joda.time.DateTime;
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
    }
}
