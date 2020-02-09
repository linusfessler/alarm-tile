package linusfessler.alarmtiles.shared;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeOfDayFormatter {

    @Inject
    public TimeOfDayFormatter() {

    }

    public String format(final int hourOfDay, final int minutesOfHour, final boolean is24Hours) {
        final Calendar calendar = new GregorianCalendar(0, 0, 0, hourOfDay, minutesOfHour);
        final Date timeOfDay = calendar.getTime();
        final SimpleDateFormat displayFormat = getFormat(is24Hours);
        return displayFormat.format(timeOfDay);
    }

    private SimpleDateFormat getFormat(final boolean is24Hours) {
        if (is24Hours) {
            return new SimpleDateFormat("H:mm", Locale.UK);
        }

        return new SimpleDateFormat("h:mm a", Locale.US);
    }
}
