package linusfessler.alarmtiles.tiles.sleeptimer

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import linusfessler.alarmtiles.shared.data.Time
import java.util.concurrent.TimeUnit

@Entity
data class SleepTimer(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        val startTimestamp: Long? = null,
        @field:Embedded val duration: Time = Time(30.0, TimeUnit.MINUTES),
        val isDecreasingVolume: Boolean = true
) {
    val millisLeft: Long
        get() {
            val millisElapsed = System.currentTimeMillis() - startTimestamp!!
            return duration.millis - millisElapsed
        }

    fun start(startTimestamp: Long, duration: Time): SleepTimer {
        return copy(
                isEnabled = true,
                startTimestamp = startTimestamp,
                duration = duration)
    }

    fun stop(): SleepTimer {
        return copy(isEnabled = false, startTimestamp = null)
    }

    fun setDecreasingVolume(isDecreasingVolume: Boolean): SleepTimer {
        return copy(isDecreasingVolume = isDecreasingVolume)
    }
}
