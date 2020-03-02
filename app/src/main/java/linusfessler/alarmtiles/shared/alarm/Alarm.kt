package linusfessler.alarmtiles.shared.alarm

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import linusfessler.alarmtiles.shared.data.Time
import linusfessler.alarmtiles.shared.data.TimeOfDay
import java.util.concurrent.TimeUnit

@Entity
data class Alarm(
        @field:PrimaryKey val id: Long,
        val isEnabled: Boolean = false,
        @field:Embedded val duration: Time = Time(8.0, TimeUnit.HOURS),
        @field:Embedded val timeOfDay: TimeOfDay = TimeOfDay(8, 0),
        val triggerTimestamp: Long = 0
) {
    val millisLeft: Long
        get() = triggerTimestamp - System.currentTimeMillis()

    fun enable(duration: Time, triggerTimestamp: Long) = copy(
            isEnabled = true,
            duration = duration,
            triggerTimestamp = triggerTimestamp
    )

    fun enable(timeOfDay: TimeOfDay, triggerTimestamp: Long) = copy(
            isEnabled = true,
            timeOfDay = timeOfDay,
            triggerTimestamp = triggerTimestamp
    )

    fun disable() = copy(isEnabled = false)
}