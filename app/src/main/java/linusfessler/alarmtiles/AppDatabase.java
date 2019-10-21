package linusfessler.alarmtiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.sample.NapAlarmTileBuilder;
import linusfessler.alarmtiles.sample.WeekendAlarmTileBuilder;
import linusfessler.alarmtiles.sample.WorkweekAlarmTileBuilder;

@Database(entities = AlarmTile.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AlarmTileDao alarmTiles();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database")
                    .build();
            instance.populate();
        }
        return instance;
    }

    private void populate() {
        Executors.newSingleThreadExecutor().submit(() -> {
            if (alarmTiles().count() == 0) {
                final AlarmTile workweekAlarmTile = new WorkweekAlarmTileBuilder().build();
                final AlarmTile weekendTimerTile = new WeekendAlarmTileBuilder().build();
                final AlarmTile napTile = new NapAlarmTileBuilder().build();

                final List<AlarmTile> alarmTiles = Arrays.asList(workweekAlarmTile, weekendTimerTile, napTile);
                alarmTiles().insert(alarmTiles);
            }
        });
    }
}
