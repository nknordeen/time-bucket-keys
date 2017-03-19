import java.util.HashSet;
import java.util.Set;

public class AllKeys {
    private Set<String> hourKeys;
    private Set<String> dayKeys;
    private Set<String> monthKeys;
    private Set<String> yearKeys;

    public AllKeys() {}

    public AllKeys(final Set<String> hourKeys, final Set<String> dayKeys, final Set<String> monthKeys, final Set<String> yearKeys) {
        this.hourKeys = hourKeys;
        this.dayKeys = dayKeys;
        this.monthKeys = monthKeys;
        this.yearKeys = yearKeys;
    }

    public Set<String> getHourKeys() {
        return this.hourKeys;
    }

    public Set<String> getDayKeys() {
        return this.dayKeys;
    }

    public Set<String> getMonthKeys() {
        return this.monthKeys;
    }

    public Set<String> getYearKeys() {
        return this.yearKeys;
    }
}
