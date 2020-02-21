package linusfessler.alarmtiles.shared

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeFormatter @Inject constructor() {
    fun format(millis: Long, precision: TimeUnit): String {
        var millisLeft = millis

        val hours = TimeUnit.MILLISECONDS.toHours(millisLeft)
        millisLeft -= TimeUnit.HOURS.toMillis(hours)

        if (precision == TimeUnit.HOURS) {
            return formatHours(hours)
        }

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisLeft)
        millisLeft -= TimeUnit.MINUTES.toMillis(minutes)

        if (precision == TimeUnit.MINUTES) {
            return format(hours, minutes)
        }

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisLeft)
        millisLeft -= TimeUnit.SECONDS.toMillis(seconds)

        if (precision == TimeUnit.SECONDS) {
            return format(hours, minutes, seconds)
        }

        if (precision == TimeUnit.MILLISECONDS) {
            return format(hours, minutes, seconds, millisLeft)
        }

        throw IllegalArgumentException("Precision $precision is not supported.")
    }

    fun format(hours: Long, minutes: Long, seconds: Long, millis: Long): String {
        if (hours == 0L && minutes == 0L && seconds == 0L) {
            return formatMillis(millis)
        }

        if (hours == 0L && minutes == 0L) {
            return formatSeconds(seconds) + " " + formatMillis(millis)
        }

        return if (hours == 0L) {
            formatMinutes(minutes) + " " + formatSeconds(seconds) + " " + formatMillis(millis)
        } else {
            formatHours(hours) + " " + formatMinutes(minutes) + " " + formatSeconds(seconds) + " " + formatMillis(millis)
        }
    }

    fun format(hours: Long, minutes: Long, seconds: Long): String {
        if (hours == 0L && minutes == 0L) {
            return formatSeconds(seconds)
        }
        
        return if (hours == 0L) {
            formatMinutes(minutes) + " " + formatSeconds(seconds)
        } else {
            formatHours(hours) + " " + formatMinutes(minutes) + " " + formatSeconds(seconds)
        }
    }

    fun format(hours: Long, minutes: Long): String {
        return if (hours == 0L) {
            formatMinutes(minutes)
        } else {
            formatHours(hours) + " " + formatMinutes(minutes)
        }
    }

    fun formatHours(hours: Long): String {
        return hours.toString() + "h"
    }

    fun formatMinutes(minutes: Long): String {
        return minutes.toString() + "m"
    }

    fun formatSeconds(seconds: Long): String {
        return seconds.toString() + "s"
    }

    fun formatMillis(millis: Long): String {
        return String.format("%02d", millis / 10)
    }
}