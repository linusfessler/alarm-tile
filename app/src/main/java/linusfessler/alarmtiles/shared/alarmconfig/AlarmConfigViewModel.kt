package linusfessler.alarmtiles.shared.alarmconfig

import android.net.Uri
import io.reactivex.subjects.BehaviorSubject
import linusfessler.alarmtiles.shared.data.Time
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmConfigViewModel @Inject constructor(private val repository: AlarmConfigRepository) {
    var config: AlarmConfig = repository.config.blockingFirst()
    private val configSubject = BehaviorSubject.createDefault(config)
    val configObservable = configSubject.hide()

    fun setAlarmSoundUri(alarmSoundUri: Uri) {
        config = config.copy(alarmSoundUri = alarmSoundUri)
        update(config)
    }

    fun setVibrate(vibrate: Boolean) {
        config = config.copy(vibrate = vibrate)
        update(config)
    }

    fun setFlashlight(flashlight: Boolean) {
        config = config.copy(flashlight = flashlight)
        update(config)
    }

    fun setSnoozeTime(snoozeTime: Double) {
        val snoozeTimeUnit = config.snoozeDuration.timeUnit
        val snoozeDuration = Time(snoozeTime, snoozeTimeUnit)
        config = config.copy(snoozeDuration = snoozeDuration)
        update(config)
    }

    fun setSnoozeTimeUnit(snoozeTimeUnit: TimeUnit) {
        val snoozeTime = config.snoozeDuration.time
        val snoozeDuration = Time(snoozeTime, snoozeTimeUnit)
        config = config.copy(snoozeDuration = snoozeDuration)
        update(config)
    }

    private fun update(config: AlarmConfig) {
        repository.update(config)
        configSubject.onNext(config)
    }
}