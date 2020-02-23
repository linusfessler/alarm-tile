package linusfessler.alarmtiles

import linusfessler.alarmtiles.shared.TimeOfDayFormatter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TimeOfDayFormatterTest {
    private val timeOfDayFormatter12 = TimeOfDayFormatter(false)
    private val timeOfDayFormatter24 = TimeOfDayFormatter(true)

    @Test
    fun format24HoursFormatAm() {
        // GIVEN
        val hourOfDay = 4
        val minutesOfHour = 45
        val expected = "4:45"
        // WHEN
        val actual = timeOfDayFormatter24.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format24HoursFormatPm() {
        // GIVEN
        val hourOfDay = 16
        val minutesOfHour = 45
        val expected = "16:45"
        // WHEN
        val actual = timeOfDayFormatter24.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format12HoursFormatAm() {
        // GIVEN
        val hourOfDay = 4
        val minutesOfHour = 45
        val expected = "4:45 AM"
        // WHEN
        val actual = timeOfDayFormatter12.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format12HoursFormatPm() {
        // GIVEN
        val hourOfDay = 16
        val minutesOfHour = 45
        val expected = "4:45 PM"
        // WHEN
        val actual = timeOfDayFormatter12.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format24HoursFormatPreviousDay() {
        // GIVEN
        val hourOfDay = -1
        val minutesOfHour = -1
        val expected = "22:59"
        // WHEN
        val actual = timeOfDayFormatter24.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format24HoursFormatNextDay() {
        // GIVEN
        val hourOfDay = 25
        val minutesOfHour = 1
        val expected = "1:01"
        // WHEN
        val actual = timeOfDayFormatter24.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format12HoursFormatMidnight() {
        // GIVEN
        val hourOfDay = 0
        val minutesOfHour = 0
        val expected = "12:00 AM"
        // WHEN
        val actual = timeOfDayFormatter12.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun format12HoursFormatMidday() {
        // GIVEN
        val hourOfDay = 12
        val minutesOfHour = 0
        val expected = "12:00 PM"
        // WHEN
        val actual = timeOfDayFormatter12.format(hourOfDay, minutesOfHour)
        // THEN
        Assertions.assertEquals(expected, actual)
    }
}
