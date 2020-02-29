package linusfessler.alarmtiles.shared

import android.net.Uri
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

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(string: String): Uri {
        return Uri.parse(string)
    }
}