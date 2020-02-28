package linusfessler.alarmtiles.shared.alarm

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepository @Inject constructor(database: AlarmDatabase) {
    private val dao = database.dao()
    val alarm = dao.select()

    fun update(alarm: Alarm) {
        GlobalScope.launch {
            dao.update(alarm)
        }
    }
}