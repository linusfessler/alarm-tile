package linusfessler.alarmtiles.shared;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.view.inputmethod.InputMethodManager;

import androidx.work.WorkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public class SharedModule {

    private final Application application;

    public SharedModule(final Application application) {
        this.application = application;
    }

    @Provides
    Application application() {
        return application;
    }

    @Provides
    @Singleton
    WorkManager workManager() {
        return WorkManager.getInstance(application);
    }

    @Provides
    @Singleton
    AudioManager audioManager() {
        return application.getSystemService(AudioManager.class);
    }

    @Provides
    @Singleton
    AlarmManager alarmManager() {
        return application.getSystemService(AlarmManager.class);
    }

    @Provides
    @Singleton
    NotificationManager notificationManager() {
        return application.getSystemService(NotificationManager.class);
    }

    @Provides
    @Singleton
    CameraManager cameraManager() {
        return application.getSystemService(CameraManager.class);
    }

    @Provides
    @Singleton
    InputMethodManager inputMethodManager() {
        return application.getSystemService(InputMethodManager.class);
    }

    @Provides
    @Singleton
    ContentResolver contentResolver() {
        return application.getContentResolver();
    }

    @Provides
    @Singleton
    Vibrator vibrator() {
        return application.getSystemService(Vibrator.class);
    }

    @Provides
    @Singleton
    Observable<Integer> volumeObservable(final ContentResolver contentResolver, final MediaVolumeManager mediaVolumeManager) {
        return Observable.create(emitter -> {
            final ContentObserver contentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(final boolean selfChange) {
                    super.onChange(selfChange);
                    final int volume = mediaVolumeManager.getVolume();
                    emitter.onNext(volume);
                }
            };

            contentResolver.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, contentObserver);

            emitter.setCancellable(() -> contentResolver.unregisterContentObserver(contentObserver));
        });
    }
}