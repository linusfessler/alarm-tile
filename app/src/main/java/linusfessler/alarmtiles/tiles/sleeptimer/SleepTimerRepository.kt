package linusfessler.alarmtiles.tiles.sleeptimer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepTimerRepository @Inject constructor(database: SleepTimerDatabase) {
    private val dao = database.dao()
    val sleepTimer = dao.select()

    fun update(sleepTimer: SleepTimer) {
        GlobalScope.launch {
            dao.update(sleepTimer)
        }
    }
}