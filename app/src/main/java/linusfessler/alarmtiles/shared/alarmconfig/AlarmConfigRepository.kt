package linusfessler.alarmtiles.shared.alarmconfig

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmConfigRepository @Inject constructor(database: AlarmConfigDatabase) {
    private val dao = database.dao()
    val config = dao.select()

    fun update(config: AlarmConfig) {
        GlobalScope.launch {
            dao.update(config)
        }
    }
}