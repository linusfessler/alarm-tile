package linusfessler.alarmtiles.tiles.alarmtimer

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters

@Database(entities = [AlarmTimer::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class AlarmTimerDatabase : RoomDatabase() {
    fun populate(): AlarmTimerDatabase {
        GlobalScope.launch {
            if (dao().count() == 0) {
                dao().insert(AlarmTimer())
            }
        }
        return this
    }

    abstract fun dao(): AlarmTimerDao
}