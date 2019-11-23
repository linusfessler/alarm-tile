package linusfessler.alarmtiles;

import java.util.Locale;

public class TimeFormatter {

    private final String hourSuffix;
    private final String hoursSuffix;
    private final String minuteSuffix;
    private final String minutesSuffix;

    public TimeFormatter(final String hourSuffix, final String hoursSuffix, final String minuteSuffix, final String minutesSuffix) {
        this.hourSuffix = hourSuffix;
        this.hoursSuffix = hoursSuffix;
        this.minuteSuffix = minuteSuffix;
        this.minutesSuffix = minutesSuffix;
    }

    public String format(final int hours, final int minutes) {
        if (hours != 0 && minutes == 0) {
            return this.formatHours(hours);
        }

        if (hours == 0 && minutes != 0) {
            return this.formatMinutes(hours);
        }

        return this.formatHours(hours) + " " + this.formatMinutes(minutes);
    }

    private String formatHours(final int hours) {
        final String suffix = hours == 1 ? this.hourSuffix : this.hoursSuffix;
        return String.format(Locale.getDefault(), "%d%s", hours, suffix);
    }

    private String formatMinutes(final int minutes) {
        final String suffix = minutes == 1 ? this.minuteSuffix : this.minutesSuffix;
        return String.format(Locale.getDefault(), "%d%s", minutes, suffix);
    }

}
