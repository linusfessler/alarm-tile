package linusfessler.alarmtiles.tiles.alarm

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import linusfessler.alarmtiles.shared.data.TimeOfDay

@Entity
data class Alarm(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        @field:Embedded val timeOfDay: TimeOfDay = TimeOfDay(8, 0),
        val triggerTimestamp: Long = 0
) {
    fun enable(timeOfDay: TimeOfDay, triggerTimestamp: Long) = copy(
            isEnabled = true,
            timeOfDay = timeOfDay,
            triggerTimestamp = triggerTimestamp
    )

    fun disable() = copy(isEnabled = false)
}