package linusfessler.alarmtiles.sleeptimer;

import android.media.AudioManager;

import androidx.core.util.Pair;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.VolumeObserver;

public class SleepTimerVolumeFader implements LifecycleObserver {

    private final AudioManager audioManager;
    private final Observable<Integer> volumeFadeObservable;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    @SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable"})
    public SleepTimerVolumeFader(final SleepTimerRepository repository, final AudioManager audioManager, final VolumeObserver volumeObserver, final Lifecycle lifecycle) {
        this.audioManager = audioManager;
        final Observable<SleepTimer> sleepTimerObservable = repository.getSleepTimer();

        final Observable<Integer> volumeObservable = sleepTimerObservable
                .switchMap(sleepTimer -> {
                    if (!sleepTimer.isEnabled()) {
                        return Observable.empty();
                    }
                    return volumeObserver.getObservable();
                });

        this.volumeFadeObservable = Observable.combineLatest(sleepTimerObservable, volumeObservable, Pair::new)
                .switchMap(sleepTimerVolumePair -> {
                    final SleepTimer sleepTimer = sleepTimerVolumePair.first;
                    final int volume = sleepTimerVolumePair.second;

                    if (!sleepTimer.isEnabled() || volume == 0) {
                        return Observable.empty();
                    }

                    final int steps = volume;
                    final long millisLeft = sleepTimer.getMillisLeft();
                    final long millisPerStep = millisLeft / steps;

                    return Observable.intervalRange(1, steps, millisPerStep, millisPerStep, TimeUnit.MILLISECONDS)
                            .map(inverseVolume -> volume - inverseVolume.intValue());
                });

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.disposable.add(this.volumeFadeObservable
                .subscribe(this::setVolume));
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
