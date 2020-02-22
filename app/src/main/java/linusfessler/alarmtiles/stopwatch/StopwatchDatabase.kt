package linusfessler.alarmtiles.stopwatch

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters

@Database(entities = [Stopwatch::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class StopwatchDatabase : RoomDatabase() {
    fun populate(): StopwatchDatabase {
        GlobalScope.launch {
            if (dao().count() == 0) {
                dao().insert(Stopwatch())
            }
        }
        return this
    }

    abstract fun dao(): StopwatchDao
}