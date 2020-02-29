package linusfessler.alarmtiles.shared.alarmconfig

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters

@Database(entities = [AlarmConfig::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class AlarmConfigDatabase : RoomDatabase() {
    fun populate(): AlarmConfigDatabase {
        GlobalScope.launch {
            if (dao().count() == 0) {
                dao().insert(AlarmConfig())
            }
        }
        return this
    }

    abstract fun dao(): AlarmConfigDao
}