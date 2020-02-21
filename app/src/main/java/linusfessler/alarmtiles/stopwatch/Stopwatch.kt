package linusfessler.alarmtiles.stopwatch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stopwatch(
        @field:PrimaryKey val id: Long? = 0,
        val isEnabled: Boolean = false,
        val startTimestamp: Long? = null,
        val stopTimestamp: Long? = null
) {
    fun toggle(): Stopwatch {
        return if (isEnabled) {
            stop()
        } else {
            start()
        }
    }

    private fun start(): Stopwatch {
        return copy(isEnabled = true, startTimestamp = System.currentTimeMillis(), stopTimestamp = null)
    }

    private fun stop(): Stopwatch {
        return copy(isEnabled = true, stopTimestamp = System.currentTimeMillis())
    }
}