package linusfessler.alarmtiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executors;

import linusfessler.alarmtiles.dao.AlarmTileDao;
import linusfessler.alarmtiles.dao.SleepTimerTileDao;
import linusfessler.alarmtiles.dao.TimerTileDao;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.SleepTimerTile;
import linusfessler.alarmtiles.model.TimerTile;

@Database(entities = {SleepTimerTile.class, AlarmTile.class, TimerTile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(final Context context) {
        if (AppDatabase.instance == null) {
            AppDatabase.instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database")
                    .build();
            AppDatabase.instance.populate();
        }
        return AppDatabase.instance;
    }

    private void populate() {
        Executors.newSingleThreadExecutor().submit(() -> {
            if (this.sleepTimerTileDao().count() == 0) {
                this.sleepTimerTileDao().insert(new SleepTimerTile());
            }

            if (this.alarmTileDao().count() == 0) {
                this.alarmTileDao().insert(new AlarmTile());
            }

            if (this.timerTileDao().count() == 0) {
                this.timerTileDao().insert(new TimerTile());
            }
        });
    }

    public abstract SleepTimerTileDao sleepTimerTileDao();

    public abstract AlarmTileDao alarmTileDao();

    public abstract TimerTileDao timerTileDao();
}
