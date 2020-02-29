package linusfessler.alarmtiles.tiles.alarmtimer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmTimerRepository @Inject constructor(database: AlarmTimerDatabase) {
    private val dao = database.dao()
    val alarmTimer = dao.select()

    fun update(alarmTimer: AlarmTimer) {
        GlobalScope.launch {
            dao.update(alarmTimer)
        }
    }
}