package linusfessler.alarmtiles;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 2;

    @Getter
    private final ExecutorService writeExecutor = Executors.newFixedThreadPool(AppDatabase.NUMBER_OF_THREADS);

    public void populate() {
        this.getWriteExecutor().submit(() -> {
            if (this.sleepTimerDao().count() == 0) {
                this.sleepTimerDao().insert(SleepTimer.createDefault());
            }

            if (this.alarmDao().count() == 0) {
                this.alarmDao().insert(new Alarm());
            }

            if (this.timerDao().count() == 0) {
                this.timerDao().insert(new Timer());
            }

            if (this.stopwatchDao().count() == 0) {
                this.stopwatchDao().insert(Stopwatch.createDefault());
            }
        });
    }

    public abstract SleepTimerDao sleepTimerDao();

    public abstract AlarmDao alarmDao();

    public abstract TimerDao timerDao();

    public abstract StopwatchDao stopwatchDao();
}
