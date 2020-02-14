package linusfessler.alarmtiles.sleeptimer

import com.spotify.mobius.Effects.effects
import com.spotify.mobius.Next
import com.spotify.mobius.Next.*
import com.spotify.mobius.Update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepTimerEventHandler
@Inject
constructor() : Update<SleepTimer, SleepTimerEvent, SleepTimerEffect> {

    override fun update(sleepTimer: SleepTimer, event: SleepTimerEvent): Next<SleepTimer, SleepTimerEffect> {
        when (event) {
            is SleepTimerEvent.Load -> return dispatch<SleepTimer, SleepTimerEffect>(effects(SleepTimerEffect.LoadFromDatabase()))

            is SleepTimerEvent.Loaded ->
                return next(event.sleepTimer,
                        if (event.sleepTimer.isEnabled)
                            effects(SleepTimerEffect.ShowNotification())
                        else
                            effects()
                )

            is SleepTimerEvent.Toggle -> return dispatch(effects(
                    if (sleepTimer.isEnabled)
                        SleepTimerEffect.Cancel()
                    else
                        SleepTimerEffect.Start()))

            is SleepTimerEvent.Start -> return dispatch(effects(SleepTimerEffect.StartWith(sleepTimer)))

            is SleepTimerEvent.StartWith -> {
                val startedSleepTimer = event.sleepTimer.start()
                val millisLeft = startedSleepTimer.millisLeft
                val effects = effects(
                        SleepTimerEffect.SaveToDatabase(startedSleepTimer),
                        SleepTimerEffect.ShowNotification(),
                        SleepTimerEffect.ScheduleVolumeDecrease(millisLeft),
                        SleepTimerEffect.ScheduleFinish(millisLeft)
                )
                return next(startedSleepTimer, effects)
            }

            is SleepTimerEvent.VolumeChanged -> {
                return if (sleepTimer.isEnabled && sleepTimer.isDecreasingVolume)
                    dispatch(effects(SleepTimerEffect.ScheduleVolumeDecrease(sleepTimer.millisLeft)))
                else
                    noChange()
            }

            is SleepTimerEvent.SetTime -> {
                val updatedSleepTimer = sleepTimer.setTime(event.time)
                val effects: MutableSet<SleepTimerEffect> = effects(SleepTimerEffect.SaveToDatabase(updatedSleepTimer))

                return if (updatedSleepTimer.isEnabled) {
                    effects.add(SleepTimerEffect.StartWith(updatedSleepTimer))
                    dispatch(effects)
                } else {
                    next(updatedSleepTimer, effects)
                }
            }

            is SleepTimerEvent.SetTimeUnit -> {
                val updatedSleepTimer = sleepTimer.setTimeUnit(event.timeUnit)
                val effects: MutableSet<SleepTimerEffect> = effects(SleepTimerEffect.SaveToDatabase(updatedSleepTimer))

                return if (updatedSleepTimer.isEnabled) {
                    effects.add(SleepTimerEffect.StartWith(updatedSleepTimer))
                    dispatch(effects)
                } else {
                    next(updatedSleepTimer, effects)
                }
            }

            is SleepTimerEvent.SetDecreasingVolume -> {
                val updatedSleepTimer = sleepTimer.setDecreasingVolume(event.decreasingVolume)
                val effects: MutableSet<SleepTimerEffect> = effects(SleepTimerEffect.SaveToDatabase(updatedSleepTimer))

                if (updatedSleepTimer.isEnabled) {
                    if (updatedSleepTimer.isDecreasingVolume) {
                        effects.add(SleepTimerEffect.ScheduleVolumeDecrease(updatedSleepTimer.millisLeft))
                    } else {
                        effects.add(SleepTimerEffect.UnscheduleVolumeDecrease())
                    }
                }

                return next(updatedSleepTimer, effects)
            }

            is SleepTimerEvent.Cancel -> {
                val stoppedSleepTimer = sleepTimer.stop()
                val effects = effects(
                        SleepTimerEffect.SaveToDatabase(stoppedSleepTimer),
                        SleepTimerEffect.HideNotification(),
                        SleepTimerEffect.UnscheduleVolumeDecrease(),
                        SleepTimerEffect.UnscheduleFinish()
                )
                return next(stoppedSleepTimer, effects)
            }

            is SleepTimerEvent.Finish -> return dispatch(effects(SleepTimerEffect.FinishWith(sleepTimer)))

            is SleepTimerEvent.FinishWith -> {
                val stoppedSleepTimer = event.sleepTimer.stop()
                val effects = effects(
                        SleepTimerEffect.SaveToDatabase(stoppedSleepTimer),
                        SleepTimerEffect.HideNotification(),
                        SleepTimerEffect.UnscheduleVolumeDecrease(),
                        SleepTimerEffect.UnscheduleFinish(),
                        SleepTimerEffect.StopMediaPlayback()
                )
                if (stoppedSleepTimer.isDecreasingVolume) {
                    effects.add(SleepTimerEffect.SetVolumeToZero())
                }
                return next(stoppedSleepTimer, effects)
            }

            else -> throw IllegalStateException("Unhandled event $event")
        }
    }
}
