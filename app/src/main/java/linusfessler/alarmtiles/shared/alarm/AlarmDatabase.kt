package linusfessler.alarmtiles.shared.alarm

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters

@Database(entities = [Alarm::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class AlarmDatabase : RoomDatabase() {
    fun populate(): AlarmDatabase {
        GlobalScope.launch {
            if (dao().count() == 0) {
                dao().insert(Alarm())
            }
        }
        return this
    }

    abstract fun dao(): AlarmDao
}