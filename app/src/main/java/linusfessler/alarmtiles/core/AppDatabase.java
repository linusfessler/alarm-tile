package linusfessler.alarmtiles.core;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.alarm.Alarm;
import linusfessler.alarmtiles.alarm.AlarmDao;
import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerDao;
import linusfessler.alarmtiles.stopwatch.Stopwatch;
import linusfessler.alarmtiles.stopwatch.StopwatchDao;
import linusfessler.alarmtiles.timer.Timer;
import linusfessler.alarmtiles.timer.TimerDao;
import lombok.Getter;

@Database(entities = {SleepTimer.class, Alarm.class, Timer.class, Stopwatch.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 2;

    @Getter
    private final ExecutorService writeExecutor = Executors.newFixedThreadPool(AppDatabase.NUMBER_OF_THREADS);

    public AppDatabase populate() {
        getWriteExecutor().submit(() -> {
            if (sleepTimerDao().count() == 0) {
                sleepTimerDao().insert(SleepTimer.createDefault());
            }

            if (alarmDao().count() == 0) {
                alarmDao().insert(new Alarm());
            }

            if (timerDao().count() == 0) {
                timerDao().insert(new Timer());
            }

            if (stopwatchDao().count() == 0) {
                stopwatchDao().insert(Stopwatch.createDefault());
            }
        });

        return this;
    }

    public abstract SleepTimerDao sleepTimerDao();

    public abstract AlarmDao alarmDao();

    public abstract TimerDao timerDao();

    public abstract StopwatchDao stopwatchDao();
}
