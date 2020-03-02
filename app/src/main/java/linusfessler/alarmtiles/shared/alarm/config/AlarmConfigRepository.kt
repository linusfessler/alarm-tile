package linusfessler.alarmtiles.shared.alarm.config

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import linusfessler.alarmtiles.shared.alarm.AlarmDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmConfigRepository @Inject constructor(database: AlarmDatabase) {
    private val dao = database.configDao()
    val config = dao.select()

    fun update(config: AlarmConfig) {
        GlobalScope.launch {
            dao.update(config)
        }
    }
}