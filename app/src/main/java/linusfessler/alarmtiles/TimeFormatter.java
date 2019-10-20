package linusfessler.alarmtiles;

import java.util.Locale;

public class TimeFormatter {

    public String format(final int hours, final int minutes) {
        String hoursString = null;
        if (hours > 0) {
            final String suffix = hours == 1 ? "hour" : "hours";
            hoursString = String.format(Locale.getDefault(), "%d %s", hours, suffix);
        }

        String minutesString = null;
        if (minutes > 0) {
            final String suffix = minutes == 1 ? "minute" : "minutes";
            minutesString = String.format(Locale.getDefault(), "%d %s", minutes, suffix);
        }

        if (hoursString == null && minutesString == null) {
            return "0 minutes";
        }

        if (hoursString == null) {
            return minutesString;
        }

        if (minutesString == null) {
            return hoursString;

        }

        return hoursString + " " + minutesString;
    }

}
