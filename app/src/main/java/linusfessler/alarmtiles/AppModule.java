package linusfessler.alarmtiles;

import android.app.Application;
import android.media.AudioManager;

import androidx.room.Room;
import androidx.work.WorkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application application;

    public AppModule(final Application application) {
        this.application = application;
    }

    @Provides
    Application application() {
        return this.application;
    }

    @Provides
    @Singleton
    AppDatabase appDatabase() {
        final AppDatabase appDatabase = Room.databaseBuilder(this.application, AppDatabase.class, "app-database").build();
        appDatabase.populate();
        return appDatabase;
    }

    @Provides
    @Singleton
    WorkManager workManager() {
        return WorkManager.getInstance(this.application);
    }

    @Provides
    @Singleton
    AudioManager audioManager() {
        return this.application.getSystemService(AudioManager.class);
    }
}