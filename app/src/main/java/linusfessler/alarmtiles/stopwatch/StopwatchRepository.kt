package linusfessler.alarmtiles.stopwatch

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchRepository @Inject constructor(database: StopwatchDatabase) {
    private val dao = database.dao()
    val stopwatch = dao.select()

    fun update(stopwatch: Stopwatch) {
        GlobalScope.launch {
            dao.update(stopwatch)
        }
    }
}