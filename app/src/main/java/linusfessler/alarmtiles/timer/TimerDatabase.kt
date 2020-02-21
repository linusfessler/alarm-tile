package linusfessler.alarmtiles.timer

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters

@Database(entities = [Timer::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class TimerDatabase : RoomDatabase() {
    fun populate(): TimerDatabase {
        GlobalScope.launch {
            if (dao().count() == 0) {
                dao().insert(Timer())
            }
        }
        return this
    }

    abstract fun dao(): TimerDao
}