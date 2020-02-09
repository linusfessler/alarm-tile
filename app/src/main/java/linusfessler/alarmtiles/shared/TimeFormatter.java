package linusfessler.alarmtiles.shared;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeFormatter {

    @Inject
    public TimeFormatter() {

    }

    public String format(final long millis, final TimeUnit precision) {
        long millisLeft = millis;

        final long hours = TimeUnit.MILLISECONDS.toHours(millisLeft);
        millisLeft -= TimeUnit.HOURS.toMillis(hours);

        if (precision == TimeUnit.HOURS) {
            return formatHours(hours);
        }

        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millisLeft);
        millisLeft -= TimeUnit.MINUTES.toMillis(minutes);

        if (precision == TimeUnit.MINUTES) {
            return format(hours, minutes);
        }

        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millisLeft);
        millisLeft -= TimeUnit.SECONDS.toMillis(seconds);

        if (precision == TimeUnit.SECONDS) {
            return format(hours, minutes, seconds);
        }

        if (precision == TimeUnit.MILLISECONDS) {
            return format(hours, minutes, seconds, millisLeft);
        }

        throw new IllegalArgumentException(String.format("Precision %s is not supported.", precision));
    }

    public String format(final long hours, final long minutes, final long seconds, final long millis) {
        if (hours == 0 && minutes == 0 && seconds == 0) {
            return formatMillis(millis);
        }

        if (hours == 0 && minutes == 0) {
            return formatSeconds(seconds) + " " + formatMillis(millis);
        }

        if (hours == 0) {
            return formatMinutes(minutes) + " " + formatSeconds(seconds) + " " + formatMillis(millis);
        }

        return formatHours(hours) + " " + formatMinutes(minutes) + " " + formatSeconds(seconds) + " " + formatMillis(millis);
    }

    public String format(final long hours, final long minutes, final long seconds) {
        if (hours == 0 && minutes == 0) {
            return formatSeconds(seconds);
        }

        if (hours == 0) {
            return formatMinutes(minutes) + " " + formatSeconds(seconds);
        }

        return formatHours(hours) + " " + formatMinutes(minutes) + " " + formatSeconds(seconds);
    }

    public String format(final long hours, final long minutes) {
        if (hours == 0) {
            return formatMinutes(minutes);
        }

        return formatHours(hours) + " " + formatMinutes(minutes);
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
