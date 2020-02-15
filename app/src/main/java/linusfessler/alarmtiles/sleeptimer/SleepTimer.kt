package linusfessler.alarmtiles.sleeptimer

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity
data class SleepTimer(@field:PrimaryKey val id: Long, val isEnabled: Boolean, val startTimestamp: Long?, val time: Double, val timeUnit: TimeUnit, val isDecreasingVolume: Boolean) {

    val millisLeft: Long
        get() {
            val millisElapsed = System.currentTimeMillis() - startTimestamp!!
            return durationMillis - millisElapsed
        }

    private val durationMillis: Long
        get() {
            return when (timeUnit) {
                TimeUnit.HOURS -> (time * 60.0 * 60.0 * 1000.0).toLong()
                TimeUnit.MINUTES -> (time * 60.0 * 1000.0).toLong()
                TimeUnit.SECONDS -> (time * 1000).toLong()
                else -> throw IllegalStateException(String.format("Unhandled time unit %s.", timeUnit))
            }
        }

    fun start(startTimestamp: Long, time: Double, timeUnit: TimeUnit): SleepTimer {
        return copy(
                isEnabled = true,
                startTimestamp = startTimestamp,
                time = time,
                timeUnit = timeUnit)
    }

    fun stop(): SleepTimer {
        return copy(isEnabled = false, startTimestamp = null)
    }

    fun setDecreasingVolume(isDecreasingVolume: Boolean): SleepTimer {
        return copy(isDecreasingVolume = isDecreasingVolume)
    }

    companion object {
        @Ignore
        fun createDefault(): SleepTimer {
            return SleepTimer(
                    0,
                    false,
                    null,
                    30.0,
                    TimeUnit.MINUTES,
                    true
            )
        }
    }
}
