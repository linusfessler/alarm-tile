package linusfessler.alarmtiles.shared;

import androidx.room.TypeConverter;

import java.util.concurrent.TimeUnit;

import linusfessler.alarmtiles.sleeptimer.SleepTimerState;

public class Converters {

    @TypeConverter
    public String fromTimeUnit(final TimeUnit timeUnit) {
        return timeUnit.toString();
    }

    @TypeConverter
    public TimeUnit toTimeUnit(final String string) {
        return TimeUnit.valueOf(string);
    }

    @TypeConverter
    public String fromSleepTimerState(final SleepTimerState sleepTimerState) {
        return sleepTimerState.toString();
    }

    @TypeConverter
    public SleepTimerState toSleepTimerState(final String string) {
        return SleepTimerState.valueOf(string);
    }
}
