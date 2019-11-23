package linusfessler.alarmtiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.dao.AlarmTileDao;
import linusfessler.alarmtiles.exampletiles.ExampleTile1Builder;
import linusfessler.alarmtiles.exampletiles.ExampleTile2Builder;
import linusfessler.alarmtiles.exampletiles.ExampleTile3Builder;
import linusfessler.alarmtiles.model.AlarmTile;

@Database(entities = {AlarmTile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AlarmTileDao alarmTiles();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(final Context context) {
        if (AppDatabase.instance == null) {
            AppDatabase.instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database")
                    .build();
            AppDatabase.instance.populate(context);
        }
        return AppDatabase.instance;
    }

    private void populate(final Context context) {
        Executors.newSingleThreadExecutor().submit(() -> {
            if (this.alarmTiles().count() == 0) {
                final AlarmTile exampleTile1 = new ExampleTile1Builder(context).build();
                final AlarmTile exampleTile2 = new ExampleTile2Builder(context).build();
                final AlarmTile exampleTile3 = new ExampleTile3Builder(context).build();

                final List<AlarmTile> alarmTiles = Arrays.asList(exampleTile1, exampleTile2, exampleTile3);
                this.alarmTiles().insert(alarmTiles);
            }
        });
    }
}
