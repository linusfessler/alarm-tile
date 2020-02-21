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
            is SleepTimerEvent.Initialize -> return dispatch(effects(SleepTimerEffect.LoadFromDatabase()))

            is SleepTimerEvent.Initialized ->
                return next(event.sleepTimer,
                        if (event.sleepTimer.isEnabled)
                            effects(SleepTimerEffect.ShowNotification())
                        else
                            effects()
                )

            is SleepTimerEvent.Start -> {
                return dispatch(effects(SleepTimerEffect.StartWith(event.time, event.timeUnit)))
            }

            is SleepTimerEvent.StartWith -> {
                val startedSleepTimer = sleepTimer.start(event.startTimestamp, event.time, event.timeUnit)
                val millisLeft = startedSleepTimer.millisLeft

                val effects = effects(
                        SleepTimerEffect.SaveToDatabase(startedSleepTimer),
                        SleepTimerEffect.ShowNotification(),
                        SleepTimerEffect.ScheduleFinish(millisLeft)
                )
                if (sleepTimer.isDecreasingVolume) {
                    effects.add(SleepTimerEffect.ScheduleVolumeDecrease(millisLeft))
                }

                return next(startedSleepTimer, effects)
            }

            is SleepTimerEvent.VolumeChanged -> {
                return if (sleepTimer.isEnabled && sleepTimer.isDecreasingVolume)
                    dispatch(effects(SleepTimerEffect.ScheduleVolumeDecrease(sleepTimer.millisLeft)))
                else
                    noChange()
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
