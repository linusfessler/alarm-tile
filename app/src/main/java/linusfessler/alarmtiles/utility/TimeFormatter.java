package linusfessler.alarmtiles.utility;

import java.util.Locale;

public class TimeFormatter {

    private TimeFormatter() {}

    public static String format(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public static String formatMilliSeconds(String millis) {
        return String.format(Locale.getDefault(), "%sms", millis);
    }

    public static String formatHoursMinutes(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02dh %02dmin", hours, minutes);
    }
}
