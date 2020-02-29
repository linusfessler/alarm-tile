package linusfessler.alarmtiles.tiles.alarm

import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.shared.AlarmClockManager
import linusfessler.alarmtiles.shared.MainActivity
import linusfessler.alarmtiles.shared.alarmconfig.AlarmBroadcastReceiver
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmEffectHandler @Inject constructor(
        private val repository: AlarmRepository,
        private val alarmClockManager: AlarmClockManager<MainActivity, AlarmBroadcastReceiver>
) : Connectable<AlarmEffect, AlarmEvent> {
    override fun connect(eventConsumer: Consumer<AlarmEvent>): Connection<AlarmEffect> {
        return object : Connection<AlarmEffect> {
            private val disposable = CompositeDisposable()

            override fun accept(effect: AlarmEffect) {
                when (effect) {
                    is AlarmEffect.LoadFromDatabase -> disposable.add(repository.alarm
                            .take(1)
                            .subscribe {
                                eventConsumer.accept(
                                        if (it.isEnabled && it.triggerTimestamp <= System.currentTimeMillis()) {
                                            // TODO: Show notification that the alarm was missed
                                            AlarmEvent.Disable()
                                        } else {
                                            AlarmEvent.Resumed(it)
                                        }
                                )
                            })

                    is AlarmEffect.SaveToDatabase -> repository.update(effect.alarm)

                    is AlarmEffect.Enable -> {
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
                        eventConsumer.accept(AlarmEvent.EnableWith(effect.timeOfDay, triggerTimestamp))
                    }

                    is AlarmEffect.Disable -> eventConsumer.accept(AlarmEvent.Disable())

                    is AlarmEffect.SetAlarm -> alarmClockManager.setAlarm(effect.triggerTimestamp)

                    is AlarmEffect.CancelAlarm -> alarmClockManager.cancelAlarm()
                }
            }

            override fun dispose() {
                disposable.dispose()
            }
        }
    }
}
