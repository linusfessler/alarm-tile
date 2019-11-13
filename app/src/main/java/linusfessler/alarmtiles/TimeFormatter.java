package linusfessler.alarmtiles;

import java.util.Locale;

public class TimeFormatter {

    private final String hourString;
    private final String hoursString;
    private final String minuteString;
    private final String minutesString;

    public TimeFormatter(final String hourString, final String hoursString, final String minuteString, final String minutesString) {
        this.hourString = hourString;
        this.hoursString = hoursString;
        this.minuteString = minuteString;
        this.minutesString = minutesString;
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
        final String hoursSuffix = hours == 1 ? this.hourString : this.hoursString;
        return String.format(Locale.getDefault(), "%d%s", hours, hoursSuffix);
    }

    private String formatMinutes(final int minutes) {
        final String minutesSuffix = minutes == 1 ? this.minuteString : this.minutesString;
        return String.format(Locale.getDefault(), "%d%s", minutes, minutesSuffix);
    }

}
