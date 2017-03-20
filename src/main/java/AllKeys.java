import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AllKeys {
    private Set<String> hourKeys;
    private Set<String> dayKeys;
    private Set<String> monthKeys;
    private Set<String> yearKeys;

    public AllKeys() {
        this.hourKeys = new HashSet<String>();
        this.dayKeys = new HashSet<String>();
        this.monthKeys = new HashSet<String>();
        this.yearKeys = new HashSet<String>();
    }

    public AllKeys(final Set<String> hourKeys, final Set<String> dayKeys, final Set<String> monthKeys, final Set<String> yearKeys) {
        this.hourKeys = hourKeys;
        this.dayKeys = dayKeys;
        this.monthKeys = monthKeys;
        this.yearKeys = yearKeys;
    }

    public void addHourKeys(final Collection<String> keys) {
        this.hourKeys.addAll(keys);
    }

    public void addDayKeys(final Collection<String> keys) {
        this.dayKeys.addAll(keys);
    }

    public void addMonthKeys(final Collection<String> keys) {
        this.monthKeys.addAll(keys);
    }

    public void addYearKeys(final Collection<String> keys) {
        this.yearKeys.addAll(keys);
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
