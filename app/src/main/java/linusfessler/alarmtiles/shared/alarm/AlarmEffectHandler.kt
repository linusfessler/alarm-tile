package linusfessler.alarmtiles.shared.alarm

import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import linusfessler.alarmtiles.shared.AlarmBroadcastReceiver
import linusfessler.alarmtiles.shared.AlarmClockManager
import linusfessler.alarmtiles.shared.MainActivity
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AlarmEffectHandler @Inject constructor(
        private val repository: AlarmRepository,
        @Named("alarmId") private val alarmId: Long,
        private val alarmClockManager: AlarmClockManager<MainActivity, AlarmBroadcastReceiver>
) : Connectable<AlarmEffect, AlarmEvent> {
    override fun connect(eventConsumer: Consumer<AlarmEvent>): Connection<AlarmEffect> {
        return object : Connection<AlarmEffect> {
            override fun accept(effect: AlarmEffect) {
                when (effect) {
                    is AlarmEffect.LoadFromDatabase -> repository
                            .select(alarmId)
                            .subscribe({
                                eventConsumer.accept(
                                        if (it.isEnabled && it.triggerTimestamp <= System.currentTimeMillis()) {
                                            // TODO: Show notification that the alarm was missed
                                            AlarmEvent.Cancel()
                                        } else {
                                            AlarmEvent.Resumed(it)
                                        }
                                )
                            }, {
                                // No alarm found in repository yet, insert it
                                val alarm = Alarm(alarmId)
                                repository.insert(alarm)
                                eventConsumer.accept(AlarmEvent.Resumed(alarm))
                            })

                    is AlarmEffect.SaveToDatabase -> repository.update(effect.alarm)

                    is AlarmEffect.SetAtTimeOfDay -> {
                        val currentTimestamp = System.currentTimeMillis()
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = currentTimestamp
                        calendar.set(Calendar.HOUR_OF_DAY, effect.timeOfDay.hourOfDay)
                        calendar.set(Calendar.MINUTE, effect.timeOfDay.minuteOfHour)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        if (calendar.timeInMillis <= currentTimestamp) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                        }
                        val triggerTimestamp = calendar.timeInMillis
                        eventConsumer.accept(AlarmEvent.SetAtTimeOfDayWithTimestamp(effect.timeOfDay, triggerTimestamp))
                    }

                    is AlarmEffect.SetAfterDuration -> {
                        val triggerTimestamp = System.currentTimeMillis() + effect.duration.millis
                        eventConsumer.accept(AlarmEvent.SetAfterDurationWithTimestamp(effect.duration, triggerTimestamp))
                    }

                    is AlarmEffect.SetAlarm -> alarmClockManager.setAlarm(effect.triggerTimestamp)

                    is AlarmEffect.CancelAlarm -> alarmClockManager.cancelAlarm()
                }
            }

            override fun dispose() {
                // Nothing to dispose of
            }
        }
    }
}
