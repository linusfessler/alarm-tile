package linusfessler.alarmtiles;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeOfDayFormatter {

    public String format(final int hourOfDay, final int minutesOfHour, final boolean is24Hours) {
        final Calendar calendar = new GregorianCalendar(0, 0, 0, hourOfDay, minutesOfHour);
        final Date timeOfDay = calendar.getTime();
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
