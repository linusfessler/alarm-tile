package linusfessler.alarmtiles;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TimeFormatterTest {

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Test
    public void formatMillisWithPrecisionMilliseconds() {
        // GIVEN
        final int millis = 123456789;
        final TimeUnit precision = TimeUnit.MILLISECONDS;
        final String expected = "34h 17m 36s 78";
        // WHEN
        final String actual = this.timeFormatter.format(millis, precision);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatMillisWithPrecisionSeconds() {
        // GIVEN
        final int millis = 123456789;
        final TimeUnit precision = TimeUnit.SECONDS;
        final String expected = "34h 17m 36s";
        // WHEN
        final String actual = this.timeFormatter.format(millis, precision);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatMillisWithPrecisionMinutes() {
        // GIVEN
        final int millis = 123456789;
        final TimeUnit precision = TimeUnit.MINUTES;
        final String expected = "34h 17m";
        // WHEN
        final String actual = this.timeFormatter.format(millis, precision);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatMillisWithPrecisionHours() {
        // GIVEN
        final int millis = 123456789;
        final TimeUnit precision = TimeUnit.HOURS;
        final String expected = "34h";
        // WHEN
        final String actual = this.timeFormatter.format(millis, precision);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatHoursMinutesSecondsMillis() {
        // GIVEN
        final int hours = 4;
        final int minutes = 15;
        final int seconds = 30;
        final int millis = 450;
        final String expected = "4h 15m 30s 45";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes, seconds, millis);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatHoursMinutesSeconds() {
        // GIVEN
        final int hours = 4;
        final int minutes = 15;
        final int seconds = 30;
        final String expected = "4h 15m 30s";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes, seconds);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatHoursMinutes() {
        // GIVEN
        final int hours = 4;
        final int minutes = 15;
        final String expected = "4h 15m";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatHours() {
        // GIVEN
        final int hours = 4;
        final int minutes = 0;
        final int seconds = 0;
        final int millis = 0;
        final String expected = "4h 0m 0s 00";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes, seconds, millis);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatMinutes() {
        // GIVEN
        final int hours = 0;
        final int minutes = 15;
        final int seconds = 0;
        final int millis = 0;
        final String expected = "15m 0s 00";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes, seconds, millis);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatSeconds() {
        // GIVEN
        final int hours = 0;
        final int minutes = 0;
        final int seconds = 30;
        final int millis = 0;
        final String expected = "30s 00";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes, seconds, millis);
        // THEN
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void formatMillis() {
        // GIVEN
        final int hours = 0;
        final int minutes = 0;
        final int seconds = 0;
        final int millis = 450;
        final String expected = "45";
        // WHEN
        final String actual = this.timeFormatter.format(hours, minutes, seconds, millis);
        // THEN
        Assert.assertEquals(expected, actual);
    }
}