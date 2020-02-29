package linusfessler.alarmtiles.tiles.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        val timestamp: Long = 0,
        val hourOfDay: Int = 8,
        val minuteOfHour: Int = 0
) {
    fun enable(timestamp: Long, hourOfDay: Int, minuteOfHour: Int) = copy(
            isEnabled = true,
            timestamp = timestamp,
            hourOfDay = hourOfDay,
            minuteOfHour = minuteOfHour
    )

    fun disable() = copy(isEnabled = false)
}