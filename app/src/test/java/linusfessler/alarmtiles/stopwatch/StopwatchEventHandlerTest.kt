package linusfessler.alarmtiles.stopwatch

import com.spotify.mobius.Effects
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class StopwatchEventHandlerTest {
    private var yesterdayTimestamp = GregorianCalendar(2000, 1, 1).timeInMillis
    private var currentTimestamp = GregorianCalendar(2000, 1, 2).timeInMillis
    private var tomorrowTimestamp = GregorianCalendar(2000, 1, 3).timeInMillis

    private val stopwatchEventHandler = StopwatchEventHandler()

    @Test
    internal fun initialize() {
        // GIVEN
        val model = Stopwatch()
        val event = StopwatchEvent.Initialize()
        // WHEN
        val next = stopwatchEventHandler.update(model, event)
        // THEN
        assertFalse(next.hasModel())
        assertTrue(next.effects() == Effects.effects(StopwatchEffect.LoadFromDatabase()))
    }

    @Test
    internal fun initialized() {
        // GIVEN
        val model = Stopwatch()
        val loadedStopwatch = Stopwatch().start(yesterdayTimestamp)
        val event = StopwatchEvent.Initialized(loadedStopwatch)
        // WHEN
        val next = stopwatchEventHandler.update(model, event)
        // THEN
        assertEquals(loadedStopwatch, next.modelUnsafe())
    }

    @Test
    internal fun toggleStarted() {
        // GIVEN
        val model = Stopwatch().start(yesterdayTimestamp)
        val event = StopwatchEvent.Toggle()
        // WHEN
        val next = stopwatchEventHandler.update(model, event)
        // THEN
        assertFalse(next.hasModel())
        assertTrue(next.effects() == Effects.effects(StopwatchEffect.Stop()))
    }

    @Test
    internal fun toggleStopped() {
        // GIVEN
        val model = Stopwatch().start(yesterdayTimestamp).stop(currentTimestamp)
        val event = StopwatchEvent.Toggle()
        // WHEN
        val next = stopwatchEventHandler.update(model, event)
        // THEN
        assertFalse(next.hasModel())
        assertTrue(next.effects() == Effects.effects(StopwatchEffect.Start()))
    }

    @Test
    internal fun start() {
        // GIVEN
        val startTimestamp = currentTimestamp
        val model = Stopwatch().stop(yesterdayTimestamp)
        val event = StopwatchEvent.Start(startTimestamp)
        // WHEN
        val next = stopwatchEventHandler.update(model, event)
        // THEN
        val startedStopwatch = model.start(startTimestamp)
        assertEquals(startedStopwatch, next.modelUnsafe())
        assertTrue(next.effects() == Effects.effects(StopwatchEffect.SaveToDatabase(startedStopwatch)))
    }

    @Test
    internal fun startAlreadyStarted() {
        // GIVEN
        val model = Stopwatch().start(yesterdayTimestamp)
        val event = StopwatchEvent.Start(currentTimestamp)
        // WHEN
        val exception = assertThrows<IllegalStateException> {
            stopwatchEventHandler.update(model, event)
        }
        // THEN
        assertEquals("Can't start stopwatch, it is already started", exception.message)
    }

    @Test
    internal fun stop() {
        // GIVEN
        val stopTimestamp = currentTimestamp
        val model = Stopwatch().start(yesterdayTimestamp)
        val event = StopwatchEvent.Stop(stopTimestamp)
        // WHEN
        val next = stopwatchEventHandler.update(model, event)
        // THEN
        val stoppedStopwatch = model.stop(stopTimestamp)
        assertEquals(stoppedStopwatch, next.modelUnsafe())
        assertTrue(next.effects() == Effects.effects(StopwatchEffect.SaveToDatabase(stoppedStopwatch)))
    }

    @Test
    internal fun stopAlreadyStopped() {
        // GIVEN
        val model = Stopwatch().stop(yesterdayTimestamp)
        val event = StopwatchEvent.Stop(currentTimestamp)
        // WHEN
        val exception = assertThrows<IllegalStateException> {
            stopwatchEventHandler.update(model, event)
        }
        // THEN
        assertEquals("Can't stop stopwatch, it is already stopped", exception.message)
    }
}
