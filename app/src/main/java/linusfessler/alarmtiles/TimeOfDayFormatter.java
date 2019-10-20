package linusfessler.alarmtiles;

import java.util.Locale;

public class TimeOfDayFormatter {

    public String format(final int hours, final int minutes) {
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

}
