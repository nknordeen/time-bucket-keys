import java.util.List;

public class AllKeys {
    private List<String> hourKeys;
    private List<String> dayKeys;
    private List<String> monthKeys;
    private List<String> yearKeys;

    public AllKeys() {}

    public AllKeys(final List<String> hourKeys, final List<String> dayKeys, final List<String> monthKeys, final List<String> yearKeys) {
        this.hourKeys = hourKeys;
        this.dayKeys = dayKeys;
        this.monthKeys = monthKeys;
        this.yearKeys = yearKeys;
    }

    public List<String> getHourKeys() {
        return this.hourKeys;
    }

    public List<String> getDayKeys() {
        return this.dayKeys;
    }

    public List<String> getMonthKeys() {
        return this.monthKeys;
    }

    public List<String> getYearKeys() {
        return this.yearKeys;
    }
}
