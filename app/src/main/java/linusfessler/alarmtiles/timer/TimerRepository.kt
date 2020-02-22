package linusfessler.alarmtiles.timer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerRepository @Inject constructor(database: TimerDatabase) {
    private val timerDao = database.dao()
    val timer = timerDao.select()

    fun update(timer: Timer) {
        GlobalScope.launch {
            timerDao.update(timer)
        }
    }
}