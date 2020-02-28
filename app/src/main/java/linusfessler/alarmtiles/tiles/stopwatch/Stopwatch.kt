package linusfessler.alarmtiles.tiles.stopwatch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stopwatch(
        @field:PrimaryKey val id: Long? = 0,
        val isEnabled: Boolean = false,
        val startTimestamp: Long? = null,
        val stopTimestamp: Long? = null
) {
    fun start(startTimestamp: Long): Stopwatch {
        return copy(isEnabled = true, startTimestamp = startTimestamp, stopTimestamp = null)
    }

    fun stop(stopTimestamp: Long): Stopwatch {
        return copy(isEnabled = false, stopTimestamp = stopTimestamp)
    }
}