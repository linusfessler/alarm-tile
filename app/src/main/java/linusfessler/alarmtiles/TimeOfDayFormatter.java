package linusfessler.alarmtiles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeOfDayFormatter {

    public String format(final int hourOfDay, final int minutesOfHour, final boolean is24Hours) {
        final Date timeOfDay = new GregorianCalendar(0, 0, 0, hourOfDay, minutesOfHour).getTime();
        final SimpleDateFormat displayFormat = this.getFormat(is24Hours);
        return displayFormat.format(timeOfDay);
    }

    private SimpleDateFormat getFormat(final boolean is24Hours) {
        if (is24Hours) {
            return new SimpleDateFormat("H:mm", Locale.UK);
        }
        return new SimpleDateFormat("h:mm a", Locale.US);
    }
}
