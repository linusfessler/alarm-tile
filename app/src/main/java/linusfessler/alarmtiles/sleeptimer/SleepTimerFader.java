package linusfessler.alarmtiles.sleeptimer;

import android.media.AudioManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SleepTimerFader implements LifecycleObserver {

    private final SleepTimerScheduler scheduler;
    private final AudioManager audioManager;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public SleepTimerFader(final SleepTimerScheduler scheduler, final AudioManager audioManager, final Lifecycle lifecycle) {
        this.scheduler = scheduler;
        this.audioManager = audioManager;

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.disposable.add(this.scheduler.getFadeObservable()
                .subscribe(volume -> this.setVolume(volume - 1)));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.disposable.dispose();
    }

    private void setVolume(final int volume) {
        if (volume >= 0) {
            this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }
}
