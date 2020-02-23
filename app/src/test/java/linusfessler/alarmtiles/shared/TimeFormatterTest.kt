package linusfessler.alarmtiles.shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

internal class TimeFormatterTest {
    private val timeFormatter = TimeFormatter()

    @Test
    internal fun formatMillisWithPrecisionMilliseconds() {
        // GIVEN
        val millis = 123456789
        val precision = TimeUnit.MILLISECONDS
        val expected = "34h 17m 36s 78"
        // WHEN
        val actual = timeFormatter.format(millis.toLong(), precision)
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatMillisWithPrecisionSeconds() {
        // GIVEN
        val millis = 123456789
        val precision = TimeUnit.SECONDS
        val expected = "34h 17m 36s"
        // WHEN
        val actual = timeFormatter.format(millis.toLong(), precision)
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatMillisWithPrecisionMinutes() {
        // GIVEN
        val millis = 123456789
        val precision = TimeUnit.MINUTES
        val expected = "34h 17m"
        // WHEN
        val actual = timeFormatter.format(millis.toLong(), precision)
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatMillisWithPrecisionHours() {
        // GIVEN
        val millis = 123456789
        val precision = TimeUnit.HOURS
        val expected = "34h"
        // WHEN
        val actual = timeFormatter.format(millis.toLong(), precision)
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatHoursMinutesSecondsMillis() {
        // GIVEN
        val hours = 4
        val minutes = 15
        val seconds = 30
        val millis = 450
        val expected = "4h 15m 30s 45"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatHoursMinutesSeconds() {
        // GIVEN
        val hours = 4
        val minutes = 15
        val seconds = 30
        val expected = "4h 15m 30s"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatHoursMinutes() {
        // GIVEN
        val hours = 4
        val minutes = 15
        val expected = "4h 15m"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatHours() {
        // GIVEN
        val hours = 4
        val minutes = 0
        val seconds = 0
        val millis = 0
        val expected = "4h 0m 0s 00"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatMinutes() {
        // GIVEN
        val hours = 0
        val minutes = 15
        val seconds = 0
        val millis = 0
        val expected = "15m 0s 00"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatSeconds() {
        // GIVEN
        val hours = 0
        val minutes = 0
        val seconds = 30
        val millis = 0
        val expected = "30s 00"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatMillis() {
        // GIVEN
        val hours = 0
        val minutes = 0
        val seconds = 0
        val millis = 450
        val expected = "45"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatZeroMillis() {
        // GIVEN
        val hours = 0
        val minutes = 0
        val seconds = 0
        val millis = 0
        val expected = "00"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong(), millis.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatZeroSeconds() {
        // GIVEN
        val hours = 0
        val minutes = 0
        val seconds = 0
        val expected = "0s"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong(), seconds.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatZeroMinutes() {
        // GIVEN
        val hours = 0
        val minutes = 0
        val expected = "0m"
        // WHEN
        val actual = timeFormatter.format(hours.toLong(), minutes.toLong())
        // THEN
        assertEquals(expected, actual)
    }

    @Test
    internal fun formatZeroHours() {
        // GIVEN
        val hours = 0
        val expected = "0h"
        // WHEN
        val actual = timeFormatter.formatHours(hours.toLong())
        // THEN
        assertEquals(expected, actual)
    }
}
