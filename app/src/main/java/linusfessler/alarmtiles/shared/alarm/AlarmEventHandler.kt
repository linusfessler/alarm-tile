package linusfessler.alarmtiles.shared.alarm

import com.spotify.mobius.Effects.effects
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmEventHandler @Inject constructor() : Update<Alarm, AlarmEvent, AlarmEffect> {
    override fun update(alarm: Alarm, event: AlarmEvent): Next<Alarm, AlarmEffect> {
        return when (event) {
            is AlarmEvent.Resume -> dispatch(effects(AlarmEffect.LoadFromDatabase()))

            is AlarmEvent.Resumed -> next(event.alarm)

            is AlarmEvent.SetAtTimeOfDay -> dispatch(effects(AlarmEffect.SetAtTimeOfDay(event.timeOfDay)))

            is AlarmEvent.SetAtTimeOfDayWithTimestamp -> {
                val enabledAlarm = alarm.enable(event.timeOfDay, event.triggerTimestamp)
                next(enabledAlarm, effects(
                        AlarmEffect.SaveToDatabase(enabledAlarm),
                        AlarmEffect.SetAlarm(event.triggerTimestamp)))
            }

            is AlarmEvent.SetAfterDuration -> dispatch(effects(AlarmEffect.SetAfterDuration(event.duration)))

            is AlarmEvent.SetAfterDurationWithTimestamp -> {
                val enabledAlarm = alarm.enable(event.duration, event.triggerTimestamp)
                next(enabledAlarm, effects(
                        AlarmEffect.SaveToDatabase(enabledAlarm),
                        AlarmEffect.SetAlarm(event.triggerTimestamp)))
            }

            is AlarmEvent.Cancel -> {
                val disabledAlarm = alarm.disable()
                next(disabledAlarm, effects(
                        AlarmEffect.SaveToDatabase(disabledAlarm),
                        AlarmEffect.CancelAlarm()))
            }

            else -> throw IllegalStateException("Unhandled event $event")
        }
    }
}
