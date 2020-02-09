package linusfessler.alarmtiles.core;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.view.inputmethod.InputMethodManager;

import androidx.room.Room;
import androidx.work.WorkManager;

import com.spotify.mobius.Mobius;
import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.android.AndroidLogger;
import com.spotify.mobius.rx2.RxEventSources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.shared.MediaVolumeManager;
import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffectHandler;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEvent;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEventHandler;

import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.load;

@Module
public class AppModule {

    private final Application application;

    public AppModule(final Application application) {
        this.application = application;
    }

    @Provides
    Application application() {
        return application;
    }

    @Provides
    @Singleton
    AppDatabase appDatabase() {
        return Room
                .databaseBuilder(application, AppDatabase.class, "app-database")
                .build()
                .populate();
    }

    @Provides
    @Singleton
    MobiusLoop<SleepTimer, SleepTimerEvent, SleepTimerEffect> sleepTimerLoop(final SleepTimerEventHandler sleepTimerEventHandler, final SleepTimerEffectHandler sleepTimerEffectHandler, final Observable<Integer> volumeObservable) {
        final MobiusLoop<SleepTimer, SleepTimerEvent, SleepTimerEffect> sleepTimerLoop = Mobius.loop(sleepTimerEventHandler, sleepTimerEffectHandler)
                .eventSource(RxEventSources.fromObservables(volumeObservable
                        .map(SleepTimerEvent::volumeChanged)))
                .logger(AndroidLogger.tag(application.getString(R.string.app_name)))
                .startFrom(SleepTimer.createDefault());

        sleepTimerLoop.dispatchEvent(load());
        return sleepTimerLoop;
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