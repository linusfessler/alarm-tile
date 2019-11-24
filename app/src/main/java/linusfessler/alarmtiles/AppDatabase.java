package linusfessler.alarmtiles;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.dao.AlarmDao;
import linusfessler.alarmtiles.dao.TimerDao;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.TimerTile;
import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerDao;
import lombok.Getter;

@Database(entities = {SleepTimer.class, AlarmTile.class, TimerTile.class}, version = 1)
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
                this.timerDao().insert(new TimerTile());
            }
        });
    }

    public abstract SleepTimerDao sleepTimerDao();

    public abstract AlarmDao alarmDao();

    public abstract TimerDao timerDao();
}
