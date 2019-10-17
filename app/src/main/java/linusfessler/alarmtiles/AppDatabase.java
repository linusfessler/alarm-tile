package linusfessler.alarmtiles;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Settings.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingsDao settingsDao();
}