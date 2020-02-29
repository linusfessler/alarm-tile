package linusfessler.alarmtiles.shared.data

import java.util.concurrent.TimeUnit

data class Time(val time: Double, val timeUnit: TimeUnit) {
    val millis: Long
        get() = when (timeUnit) {
            TimeUnit.HOURS -> (time * 60.0 * 60.0 * 1000.0).toLong()
            TimeUnit.MINUTES -> (time * 60.0 * 1000.0).toLong()
            TimeUnit.SECONDS -> (time * 1000).toLong()
            else -> throw IllegalStateException(String.format("Unhandled time unit %s.", timeUnit))
        }
}