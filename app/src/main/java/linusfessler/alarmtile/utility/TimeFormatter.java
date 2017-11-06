package linusfessler.alarmtile.utility;

import java.util.Locale;

public class TimeFormatter {

    private TimeFormatter() {}

    public static String format(int i, int j) {
        return String.format(Locale.getDefault(), "%02d:%02d", i, j);
    }

    public static String formatMillis(String millis) {
        return String.format(Locale.getDefault(), "%sms", millis);
    }

    public static String formatHoursMinutes(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02dh:%02dm", hours, minutes);
    }
}
