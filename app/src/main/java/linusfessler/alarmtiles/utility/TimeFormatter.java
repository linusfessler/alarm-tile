package linusfessler.alarmtiles.utility;

import java.util.Locale;

public class TimeFormatter {

    private TimeFormatter() {}

    public static String format(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public static String formatMilliSeconds(String milliseconds) {
        return String.format(Locale.getDefault(), "%sms", milliseconds);
    }

    public static String formatHoursMinutes(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02dh %02dmin", hours, minutes);
    }

    public static String formatMinutesSeconds(int minutes, int seconds) {
        return String.format(Locale.getDefault(), "%02dmin %02ds", minutes, seconds);
    }
}
