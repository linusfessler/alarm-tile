package linusfessler.alarmtiles;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    public String format(final long millis, final boolean showMillis) {
        long millisLeft = millis;

        final int hours = (int) TimeUnit.MILLISECONDS.toHours(millisLeft);
        millisLeft -= (int) TimeUnit.HOURS.toMillis(hours);

        final int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millisLeft);
        millisLeft -= (int) TimeUnit.MINUTES.toMillis(minutes);

        final int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millisLeft);
        millisLeft -= (int) TimeUnit.SECONDS.toMillis(seconds);

        final String timeWithoutMillis = this.format(hours, minutes, seconds);
        if (!showMillis) {
            return timeWithoutMillis;
        }

        return String.format(Locale.getDefault(), "%s %02d", timeWithoutMillis, millisLeft / 10);
    }

    public String format(final int hours, final int minutes, final int seconds) {
        if (hours == 0 && minutes == 0) {
            return this.formatSeconds(seconds);
        }

        if (hours == 0) {
            return this.formatMinutes(minutes) + " " + this.formatSeconds(seconds);
        }

        return this.formatHours(hours) + " " + this.formatMinutes(minutes);
    }

    public String format(final int hours, final int minutes) {
        if (hours == 0) {
            return this.formatMinutes(minutes);
        }

        return this.formatHours(hours) + " " + this.formatMinutes(minutes);
    }

    public String formatHours(final int hours) {
        return String.format(Locale.getDefault(), "%d%s", hours, "h");
    }

    public String formatMinutes(final int minutes) {
        return String.format(Locale.getDefault(), "%d%s", minutes, "m");
    }

    public String formatSeconds(final int seconds) {
        return String.format(Locale.getDefault(), "%d%s", seconds, "s");
    }
}
