package linusfessler.alarmtiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeOfDayFormatter {

    public String format(final int hourOfDay, final int minutesOfHour, final boolean is24Hours) {
        if (is24Hours) {
            return String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minutesOfHour);
        }

        final SimpleDateFormat parseFormat = new SimpleDateFormat("H:m", Locale.UK);
        final SimpleDateFormat displayFormat = new SimpleDateFormat("h:mm a", Locale.US);
        final Date date;
        try {
            date = parseFormat.parse(hourOfDay + ":" + minutesOfHour);
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e);
        }
        return displayFormat.format(date);
    }

}
