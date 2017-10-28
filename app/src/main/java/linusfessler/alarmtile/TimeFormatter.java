package linusfessler.alarmtile;

import java.util.Locale;

public class TimeFormatter {

    private TimeFormatter() {}

    public static String format(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public static String formatLabelled(int hours, int minutes) {
        return String.format(Locale.getDefault(), "%02dh:%02dm", hours, minutes);
    }
}
