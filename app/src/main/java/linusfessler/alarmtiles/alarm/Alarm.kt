package linusfessler.alarmtiles.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        val hourOfDay: Int = 7,
        val minuteOfHour: Int = 0
) {
    fun toggle(): Alarm {
        return if (isEnabled)
            disable()
        else
            enable()
    }

    private fun enable(): Alarm {
        return copy(isEnabled = true)
    }

    private fun disable(): Alarm {
        return copy(isEnabled = false)
    }
}