package linusfessler.alarmtiles.timer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer(
        @field:PrimaryKey val id: Long = 0,
        val isEnabled: Boolean = false,
        val duration: Long = 10000,
        val startTimeStamp: Long? = null
) {
    fun toggle(): Timer {
        return if (isEnabled)
            disable()
        else
            enable()
    }

    private fun enable(): Timer {
        return copy(isEnabled = true, startTimeStamp = System.currentTimeMillis())
    }

    fun disable(): Timer {
        return copy(isEnabled = false, startTimeStamp = null)
    }
}