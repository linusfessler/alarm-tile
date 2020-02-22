package linusfessler.alarmtiles.sleeptimer

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters

@Database(entities = [SleepTimer::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class SleepTimerDatabase : RoomDatabase() {
    fun populate(): SleepTimerDatabase {
        GlobalScope.launch {
            if (dao().count() == 0) {
                dao().insert(SleepTimer())
            }
        }
        return this
    }

    abstract fun dao(): SleepTimerDao
}