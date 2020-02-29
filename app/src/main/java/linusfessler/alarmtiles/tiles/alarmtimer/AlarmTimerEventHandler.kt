package linusfessler.alarmtiles.tiles.alarmtimer

import com.spotify.mobius.Effects.effects
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmTimerEventHandler @Inject constructor() : Update<AlarmTimer, AlarmTimerEvent, AlarmTimerEffect> {
    override fun update(alarm: AlarmTimer, event: AlarmTimerEvent): Next<AlarmTimer, AlarmTimerEffect> {
        return when (event) {
            is AlarmTimerEvent.Resume -> dispatch(effects(AlarmTimerEffect.LoadFromDatabase()))

            is AlarmTimerEvent.Resumed -> next(event.alarmTimer)

            is AlarmTimerEvent.Enable -> dispatch(effects(AlarmTimerEffect.Enable(event.duration)))

            is AlarmTimerEvent.EnableWith -> {
                val enabledAlarm = alarm.enable(event.duration, event.triggerTimestamp)
                next(enabledAlarm, effects(
                        AlarmTimerEffect.SaveToDatabase(enabledAlarm),
                        AlarmTimerEffect.SetAlarm(event.triggerTimestamp)))
            }

            is AlarmTimerEvent.Disable -> {
                val disabledAlarm = alarm.disable()
                next(disabledAlarm, effects(
                        AlarmTimerEffect.SaveToDatabase(disabledAlarm),
                        AlarmTimerEffect.CancelAlarm()))
            }

            else -> throw IllegalStateException("Unhandled event $event")
        }
    }
}
