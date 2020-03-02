package linusfessler.alarmtiles.shared.alarm

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.RoomTypeConverters
import linusfessler.alarmtiles.shared.alarm.config.AlarmConfig
import linusfessler.alarmtiles.shared.alarm.config.AlarmConfigDao

@Database(entities = [
    Alarm::class,
    AlarmConfig::class
], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class AlarmDatabase : RoomDatabase() {
    fun populate(): AlarmDatabase {
        GlobalScope.launch {
            if (configDao().count() == 0) {
                configDao().insert(AlarmConfig())
            }
        }
        return this
    }

    abstract fun alarmDao(): AlarmDao
    abstract fun configDao(): AlarmConfigDao
}