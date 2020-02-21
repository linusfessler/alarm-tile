package linusfessler.alarmtiles.sleeptimer

import android.app.Application
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.shared.MediaPlaybackManager
import linusfessler.alarmtiles.shared.MediaVolumeManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepTimerEffectHandler
@Inject
constructor(
        private val application: Application,
        private val repository: SleepTimerRepository,
        private val mediaVolumeManager: MediaVolumeManager,
        private val mediaPlaybackManager: MediaPlaybackManager
) : Connectable<SleepTimerEffect, SleepTimerEvent> {

    override fun connect(eventConsumer: Consumer<SleepTimerEvent>): Connection<SleepTimerEffect> {
        return object : Connection<SleepTimerEffect> {

            private val disposable = CompositeDisposable()
            private val volumeDisposable = CompositeDisposable()
            private val finishDisposable = CompositeDisposable()

            override fun accept(effect: SleepTimerEffect) {
                when (effect) {
                    is SleepTimerEffect.LoadFromDatabase -> disposable.add(repository.sleepTimer
                            .take(1)
                            .subscribe { sleepTimer ->
                                eventConsumer.accept(
                                        // Can't use Finish event since we don't want this invalid sleep timer to become our state
                                        if (sleepTimer.isEnabled && sleepTimer.millisLeft <= 0)
                                            SleepTimerEvent.FinishWith(sleepTimer)
                                        else
                                            SleepTimerEvent.Initialized(sleepTimer))
                            })

                    is SleepTimerEffect.SaveToDatabase -> repository.update(effect.sleepTimer)

                    is SleepTimerEffect.StartWith -> {
                        val startTimestamp = System.currentTimeMillis()
                        eventConsumer.accept(SleepTimerEvent.StartWith(startTimestamp, effect.time, effect.timeUnit))
                    }

                    is SleepTimerEffect.FinishWith -> eventConsumer.accept(SleepTimerEvent.FinishWith(effect.sleepTimer))

                    is SleepTimerEffect.ScheduleVolumeDecrease -> {
                        val volume = mediaVolumeManager.volume
                        val millisLeft = effect.millisLeft
                        val millisPerStep = millisLeft / volume

                        volumeDisposable.clear()
                        volumeDisposable.add(Observable
                                .intervalRange(1, volume.toLong(), millisPerStep, millisPerStep, TimeUnit.MILLISECONDS)
                                .map { inverseVolume -> volume - inverseVolume.toInt() }
                                .subscribe { mediaVolumeManager.volume = it })
                    }

                    is SleepTimerEffect.UnscheduleVolumeDecrease -> volumeDisposable.clear()

                    is SleepTimerEffect.ScheduleFinish -> {
                        finishDisposable.clear()
                        finishDisposable.add(Observable
                                .timer(effect.millisLeft, TimeUnit.MILLISECONDS)
                                .subscribe { eventConsumer.accept(SleepTimerEvent.Finish()) })
                    }

                    is SleepTimerEffect.UnscheduleFinish -> finishDisposable.clear()

                    is SleepTimerEffect.SetVolumeToZero -> mediaVolumeManager.volume = 0

                    is SleepTimerEffect.StopMediaPlayback -> mediaPlaybackManager.stopMediaPlayback()

                    is SleepTimerEffect.ShowNotification -> SleepTimerNotificationService.start(application)

                    is SleepTimerEffect.HideNotification -> SleepTimerNotificationService.stop(application)
                }
            }

            override fun dispose() {
                disposable.dispose()
            }
        }
    }
}
