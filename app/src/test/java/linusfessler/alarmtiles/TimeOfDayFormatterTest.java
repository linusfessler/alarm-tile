package linusfessler.alarmtiles;

import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayFormatterTest {

    private final TimeOfDayFormatter timeOfDayFormatter = new TimeOfDayFormatter();

    @Test
    public void format24HoursFormatAm() {
        // GIVEN
        final int hourOfDay = 4;
        final int minutesOfHour = 45;
        final boolean is24Hours = true;
        final String expected = "4:45";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format24HoursFormatPm() {
        // GIVEN
        final int hourOfDay = 16;
        final int minutesOfHour = 45;
        final boolean is24Hours = true;
        final String expected = "16:45";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format12HoursFormatAm() {
        // GIVEN
        final int hourOfDay = 4;
        final int minutesOfHour = 45;
        final boolean is24Hours = false;
        final String expected = "4:45 AM";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format12HoursFormatPm() {
        // GIVEN
        final int hourOfDay = 16;
        final int minutesOfHour = 45;
        final boolean is24Hours = false;
        final String expected = "4:45 PM";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format24HoursFormatPreviousDay() {
        // GIVEN
        final int hourOfDay = -1;
        final int minutesOfHour = -1;
        final boolean is24Hours = true;
        final String expected = "22:59";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format24HoursFormatNextDay() {
        // GIVEN
        final int hourOfDay = 25;
        final int minutesOfHour = 1;
        final boolean is24Hours = true;
        final String expected = "1:01";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format12HoursFormatMidnight() {
        // GIVEN
        final int hourOfDay = 0;
        final int minutesOfHour = 0;
        final boolean is24Hours = false;
        final String expected = "12:00 AM";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void format12HoursFormatMidday() {
        // GIVEN
        final int hourOfDay = 12;
        final int minutesOfHour = 0;
        final boolean is24Hours = false;
        final String expected = "12:00 PM";
        // WHEN
        final String actual = this.timeOfDayFormatter.format(hourOfDay, minutesOfHour, is24Hours);
        // THEN
        Assert.assertEquals(expected, actual);
    }
}