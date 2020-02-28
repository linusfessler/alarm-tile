package linusfessler.alarmtiles.tiles.stopwatch

import com.spotify.mobius.Effects
import com.spotify.mobius.test.UpdateSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*


internal class StopwatchEventHandlerTest {
    private val timestamp1 = GregorianCalendar(2000, 1, 1).timeInMillis
    private val timestamp2 = GregorianCalendar(2000, 1, 2).timeInMillis
    private val timestamp3 = GregorianCalendar(2000, 1, 3).timeInMillis

    private val updateSpec = UpdateSpec(StopwatchEventHandler())

    @Test
    internal fun initialize() {
        val stopwatch = Stopwatch()
        updateSpec
                .given(stopwatch)
                .whenEvent(StopwatchEvent.Initialize())
                .then {
                    assertEquals(stopwatch, it.model())
                    assertEquals(Effects.effects(StopwatchEffect.LoadFromDatabase()), it.lastNext().effects())
                }
    }

    @Test
    internal fun initialized() {
        val stopwatch = Stopwatch()
        val initializedStopwatch = Stopwatch().start(timestamp1)
        updateSpec
                .given(stopwatch)
                .whenEvent(StopwatchEvent.Initialized(initializedStopwatch))
                .then {
                    assertEquals(initializedStopwatch, it.model())
                    assertTrue(it.lastNext().effects().isEmpty())
                }
    }

    @Test
    internal fun toggleStarted() {
        val startedStopwatch = Stopwatch().start(timestamp1)
        updateSpec
                .given(startedStopwatch)
                .whenEvent(StopwatchEvent.Toggle())
                .then {
                    assertEquals(startedStopwatch, it.model())
                    assertEquals(Effects.effects(StopwatchEffect.Stop()), it.lastNext().effects())
                }
    }

    @Test
    internal fun toggleStopped() {
        val stoppedStopwatch = Stopwatch().start(timestamp1).stop(timestamp2)
        updateSpec
                .given(stoppedStopwatch)
                .whenEvent(StopwatchEvent.Toggle())
                .then {
                    assertEquals(stoppedStopwatch, it.model())
                    assertEquals(Effects.effects(StopwatchEffect.Start()), it.lastNext().effects())
                }
    }

    @Test
    internal fun start() {
        val stoppedStopwatch = Stopwatch().start(timestamp1).stop(timestamp2)
        val startedStopwatch = Stopwatch().start(timestamp3)
        updateSpec
                .given(stoppedStopwatch)
                .whenEvent(StopwatchEvent.Start(timestamp3))
                .then {
                    assertEquals(startedStopwatch, it.model())
                    assertEquals(Effects.effects(StopwatchEffect.SaveToDatabase(startedStopwatch)), it.lastNext().effects())
                }
    }

    @Test
    internal fun startAlreadyStarted() {
        val startedStopwatch = Stopwatch().start(timestamp1)
        updateSpec
                .given(startedStopwatch)
                .whenEvent(StopwatchEvent.Start(timestamp2))
                .thenError {
                    assertEquals("Can't start stopwatch, it is already started", it.message)
                }
    }

    @Test
    internal fun stop() {
        val startedStopwatch = Stopwatch().start(timestamp1)
        val stoppedStopwatch = startedStopwatch.stop(timestamp2)
        updateSpec
                .given(startedStopwatch)
                .whenEvent(StopwatchEvent.Stop(timestamp2))
                .then {
                    assertEquals(stoppedStopwatch, it.model())
                    assertEquals(Effects.effects(StopwatchEffect.SaveToDatabase(stoppedStopwatch)), it.lastNext().effects())
                }
    }

    @Test
    internal fun stopAlreadyStopped() {
        val stoppedStopwatch = Stopwatch().start(timestamp1).stop(timestamp2)
        updateSpec
                .given(stoppedStopwatch)
                .whenEvent(StopwatchEvent.Stop(timestamp3))
                .thenError {
                    assertEquals("Can't stop stopwatch, it is already stopped", it.message)
                }
    }
}
