package linusfessler.alarmtiles.core;

import androidx.room.TypeConverter;

import java.util.concurrent.TimeUnit;

public class Converters {

    @TypeConverter
    public String fromTimeUnit(final TimeUnit timeUnit) {
        return timeUnit.toString();
    }

    @TypeConverter
    public TimeUnit toTimeUnit(final String string) {
        return TimeUnit.valueOf(string);
    }
}
