package linusfessler.alarmtiles.tiles.alarmtimer

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import linusfessler.alarmtiles.shared.data.Time
import java.util.concurrent.TimeUnit

@Entity
data class AlarmTimer(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        @field:Embedded val duration: Time = Time(8.0, TimeUnit.HOURS),
        val triggerTimestamp: Long = 0
) {
    val millisLeft: Long
        get() = triggerTimestamp - System.currentTimeMillis()

    fun enable(duration: Time, triggerTimestamp: Long) = copy(
            isEnabled = true,
            duration = duration,
            triggerTimestamp = triggerTimestamp
    )

    fun disable() = copy(isEnabled = false)
}