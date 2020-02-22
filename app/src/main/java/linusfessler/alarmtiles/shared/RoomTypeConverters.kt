package linusfessler.alarmtiles.shared

import androidx.room.TypeConverter
import java.util.concurrent.TimeUnit

class RoomTypeConverters {
    @TypeConverter
    fun fromTimeUnit(timeUnit: TimeUnit): String {
        return timeUnit.toString()
    }

    @TypeConverter
    fun toTimeUnit(string: String): TimeUnit {
        return TimeUnit.valueOf(string)
    }
}