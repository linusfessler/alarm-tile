package linusfessler.alarmtiles;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.dao.AlarmDao;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerDao;
import linusfessler.alarmtiles.stopwatch.Stopwatch;
import linusfessler.alarmtiles.stopwatch.StopwatchDao;
import linusfessler.alarmtiles.timer.Timer;
import linusfessler.alarmtiles.timer.TimerDao;
import lombok.Getter;

@Database(entities = {SleepTimer.class, AlarmTile.class, Timer.class, Stopwatch.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 2;

    private static AppDatabase instance;

    @Getter
    private final ExecutorService writeExecutor =
            Executors.newFixedThreadPool(AppDatabase.NUMBER_OF_THREADS);

    public static synchronized AppDatabase getInstance(final Application application) {
        if (AppDatabase.instance == null) {
            AppDatabase.instance = Room
                    .databaseBuilder(application, AppDatabase.class, "app-database")
                    .build();
            AppDatabase.instance.populate();
        }
        return AppDatabase.instance;
    }

    private void populate() {
        this.getWriteExecutor().submit(() -> {
            if (this.sleepTimerDao().count() == 0) {
                this.sleepTimerDao().insert(new SleepTimer());
            }

            if (this.alarmDao().count() == 0) {
                this.alarmDao().insert(new AlarmTile());
            }

            if (this.timerDao().count() == 0) {
                this.timerDao().insert(new Timer());
            }

            if (this.stopwatchDao().count() == 0) {
                this.stopwatchDao().insert(new Stopwatch());
            }
        });
    }

    public abstract SleepTimerDao sleepTimerDao();

    public abstract AlarmDao alarmDao();

    public abstract TimerDao timerDao();

    public abstract StopwatchDao stopwatchDao();
}
