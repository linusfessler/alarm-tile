package linusfessler.alarmtiles.shared.alarm

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepository @Inject constructor(database: AlarmDatabase) {
    private val dao = database.alarmDao()

    fun select(id: Long) = dao.select(id)

    fun insert(alarm: Alarm) {
        GlobalScope.launch {
            dao.insert(alarm)
        }
    }

    fun update(alarm: Alarm) {
        GlobalScope.launch {
            dao.update(alarm)
        }
    }
}