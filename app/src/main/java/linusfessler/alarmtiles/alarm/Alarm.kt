package linusfessler.alarmtiles.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        val hourOfDay: Int = 7,
        val minuteOfHour: Int = 0,
        val triggerTimestamp: Long? = null
) {
    fun enable(hourOfDay: Int, minuteOfHour: Int, triggerTimestamp: Long): Alarm {
        return copy(isEnabled = true, hourOfDay = hourOfDay, minuteOfHour = minuteOfHour, triggerTimestamp = triggerTimestamp)
    }

    fun disable(): Alarm {
        return copy(isEnabled = false, triggerTimestamp = null)
    }
}