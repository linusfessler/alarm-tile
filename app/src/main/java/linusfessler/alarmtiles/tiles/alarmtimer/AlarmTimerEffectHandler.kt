package linusfessler.alarmtiles.tiles.alarmtimer

import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.shared.AlarmClockManager
import linusfessler.alarmtiles.shared.MainActivity
import linusfessler.alarmtiles.shared.alarmconfig.AlarmBroadcastReceiver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmTimerEffectHandler @Inject constructor(
        private val repository: AlarmTimerRepository,
        private val alarmClockManager: AlarmClockManager<MainActivity, AlarmBroadcastReceiver>
) : Connectable<AlarmTimerEffect, AlarmTimerEvent> {
    override fun connect(eventConsumer: Consumer<AlarmTimerEvent>): Connection<AlarmTimerEffect> {
        return object : Connection<AlarmTimerEffect> {
            private val disposable = CompositeDisposable()

            override fun accept(effect: AlarmTimerEffect) {
                when (effect) {
                    is AlarmTimerEffect.LoadFromDatabase -> disposable.add(repository.alarmTimer
                            .take(1)
                            .subscribe {
                                eventConsumer.accept(
                                        if (it.isEnabled && it.triggerTimestamp <= System.currentTimeMillis()) {
                                            // TODO: Show notification that the alarm was missed
                                            AlarmTimerEvent.Disable()
                                        } else {
                                            AlarmTimerEvent.Resumed(it)
                                        }
                                )
                            })

                    is AlarmTimerEffect.SaveToDatabase -> repository.update(effect.alarmTimer)

                    is AlarmTimerEffect.Enable -> {
                        val triggerTimestamp = System.currentTimeMillis() + effect.duration.millis
                        eventConsumer.accept(AlarmTimerEvent.EnableWith(effect.duration, triggerTimestamp))
                    }

                    is AlarmTimerEffect.Disable -> eventConsumer.accept(AlarmTimerEvent.Disable())

                    is AlarmTimerEffect.SetAlarm -> alarmClockManager.setAlarm(effect.triggerTimestamp)

                    is AlarmTimerEffect.CancelAlarm -> alarmClockManager.cancelAlarm()
                }
            }

            override fun dispose() {
                disposable.dispose()
            }
        }
    }
}
