package linusfessler.alarmtiles;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    public String format(final long millis, final TimeUnit precision) {
        long millisLeft = millis;

        final long hours = TimeUnit.MILLISECONDS.toHours(millisLeft);
        millisLeft -= TimeUnit.HOURS.toMillis(hours);

        if (precision == TimeUnit.HOURS) {
            return this.formatHours(hours);
        }

        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millisLeft);
        millisLeft -= TimeUnit.MINUTES.toMillis(minutes);

        if (precision == TimeUnit.MINUTES) {
            return this.format(hours, minutes);
        }

        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millisLeft);
        millisLeft -= TimeUnit.SECONDS.toMillis(seconds);

        if (precision == TimeUnit.SECONDS) {
            return this.format(hours, minutes, seconds);
        }

        if (precision == TimeUnit.MILLISECONDS) {
            return this.format(hours, minutes, seconds, millisLeft);
        }

        throw new IllegalArgumentException(String.format("Precision %s is not supported.", precision));
    }

    public String format(final long hours, final long minutes, final long seconds, final long millis) {
        if (hours == 0 && minutes == 0 && seconds == 0) {
            return this.formatMillis(millis);
        }

        if (hours == 0 && minutes == 0) {
            return this.formatSeconds(seconds) + " " + this.formatMillis(millis);
        }

        if (hours == 0) {
            return this.formatMinutes(minutes) + " " + this.formatSeconds(seconds) + " " + this.formatMillis(millis);
        }

        return this.formatHours(hours) + " " + this.formatMinutes(minutes) + " " + this.formatSeconds(seconds) + " " + this.formatMillis(millis);
    }

    public String format(final long hours, final long minutes, final long seconds) {
        if (hours == 0 && minutes == 0) {
            return this.formatSeconds(seconds);
        }

        if (hours == 0) {
            return this.formatMinutes(minutes) + " " + this.formatSeconds(seconds);
        }

        return this.formatHours(hours) + " " + this.formatMinutes(minutes) + " " + this.formatSeconds(seconds);
    }

    public String format(final long hours, final long minutes) {
        if (hours == 0) {
            return this.formatMinutes(minutes);
        }

        return this.formatHours(hours) + " " + this.formatMinutes(minutes);
    }

    public String formatHours(final long hours) {
        return hours + "h";
    }

    public String formatMinutes(final long minutes) {
        return minutes + "m";
    }

    public String formatSeconds(final long seconds) {
        return seconds + "s";
    }

    public String formatMillis(final long millis) {
        return String.format(Locale.getDefault(), "%02d", millis / 10);
    }
}
