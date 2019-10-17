package linusfessler.alarmtiles;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-database").build();
    }

    public AppDatabase getDb() {
        return db;
    }
}
