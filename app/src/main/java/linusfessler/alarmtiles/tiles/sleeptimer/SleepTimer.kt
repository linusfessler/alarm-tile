package linusfessler.alarmtiles.tiles.sleeptimer

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity
data class SleepTimer(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        val startTimestamp: Long? = null,
        val time: Double = 30.0,
        val timeUnit: TimeUnit = TimeUnit.MINUTES,
        val isDecreasingVolume: Boolean = true
) {
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
}
