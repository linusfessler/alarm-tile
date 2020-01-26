package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class SleepTimerStopService implements LifecycleObserver {

    private final Application application;
    private final AudioManager audioManager;
    private final Observable<SleepTimer> finishObservable;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public SleepTimerStopService(final Application application, final SleepTimerRepository repository, final AudioManager audioManager, final Lifecycle lifecycle) {
        this.application = application;
        this.audioManager = audioManager;

        this.finishObservable = repository.getSleepTimer()
                .switchMap(sleepTimer -> {
                    if (sleepTimer.isEnabled()) {
                        return Observable.timer(sleepTimer.getMillisLeft(), TimeUnit.MILLISECONDS)
                                .map(zero -> sleepTimer);
                    }

                    if (sleepTimer.isCancelled()) {
                        return Observable.empty();
                    }

                    return Observable.just(sleepTimer);
                });

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.disposable.add(this.finishObservable.subscribe(sleepTimer -> {
            this.stopMediaPlayback();
            SleepTimerService.stop(this.application);
        }));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.disposable.dispose();
    }

    private void stopMediaPlayback() {
        this.requestAudioFocus();
    }

    @SuppressWarnings("squid:CallToDeprecatedMethod")
    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            final AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .build();

            this.audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            this.audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }
}
