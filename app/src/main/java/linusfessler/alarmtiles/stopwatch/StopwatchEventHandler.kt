package linusfessler.alarmtiles.stopwatch

import com.spotify.mobius.Effects.effects
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopwatchEventHandler @Inject constructor() : Update<Stopwatch, StopwatchEvent, StopwatchEffect> {
    override fun update(stopwatch: Stopwatch, event: StopwatchEvent): Next<Stopwatch, StopwatchEffect> {
        return when (event) {
            is StopwatchEvent.Initialize -> dispatch(effects(StopwatchEffect.LoadFromDatabase()))

            is StopwatchEvent.Initialized -> next(event.stopwatch)

            is StopwatchEvent.Toggle -> dispatch(effects(
                    if (stopwatch.isEnabled) {
                        StopwatchEffect.Stop()
                    } else {
                        StopwatchEffect.Start()
                    }))

            is StopwatchEvent.Start -> next(stopwatch.start(event.startTimestamp))

            is StopwatchEvent.Stop -> next(stopwatch.stop(event.stopTimestamp))

            else -> throw IllegalStateException("Unhandled event $event")
        }
    }
}
