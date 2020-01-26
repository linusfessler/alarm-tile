package linusfessler.alarmtiles;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.media.AudioManager;
import android.view.inputmethod.InputMethodManager;

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

    @Provides
    @Singleton
    AlarmManager alarmManager() {
        return this.application.getSystemService(AlarmManager.class);
    }

    @Provides
    @Singleton
    NotificationManager notificationManager() {
        return this.application.getSystemService(NotificationManager.class);
    }

    @Provides
    @Singleton
    InputMethodManager inputMethodManager() {
        return this.application.getSystemService(InputMethodManager.class);
    }

    @Provides
    @Singleton
    ContentResolver contentResolver() {
        return this.application.getContentResolver();
    }
}