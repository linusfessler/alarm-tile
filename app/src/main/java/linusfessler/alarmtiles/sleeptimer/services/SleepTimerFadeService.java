package linusfessler.alarmtiles.sleeptimer.services;

import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.MediaVolumeManager;
import linusfessler.alarmtiles.VolumeObserver;
import linusfessler.alarmtiles.sleeptimer.SleepTimerRepository;
import linusfessler.alarmtiles.sleeptimer.SleepTimerServiceScope;
import linusfessler.alarmtiles.sleeptimer.SleepTimerState;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimer;

@SleepTimerServiceScope
public class SleepTimerFadeService implements LifecycleObserver {

    private final MediaVolumeManager mediaVolumeManager;
    private final Observable<Integer> fadeObservable;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    @SuppressWarnings("UnnecessaryLocalVariable")
    public SleepTimerFadeService(final SleepTimerRepository repository, final MediaVolumeManager mediaVolumeManager, final VolumeObserver volumeObserver, final Lifecycle lifecycle) {
        this.mediaVolumeManager = mediaVolumeManager;

        final Observable<SleepTimer> sleepTimerObservable = repository.getSleepTimer();
        final Observable<Integer> volumeObservable = volumeObserver.getObservable();

        final Observable<Pair<SleepTimer, Integer>> sleepTimerVolumePairObservable = Observable.combineLatest(sleepTimerObservable, volumeObservable, Pair::new);

        this.fadeObservable = sleepTimerVolumePairObservable
                .switchMap(sleepTimerVolumePair -> {
                    final SleepTimer sleepTimer = sleepTimerVolumePair.first;
                    final int volume = sleepTimerVolumePair.second;

                    if (sleepTimer.getState() == SleepTimerState.RUNNING && sleepTimer.getConfig().isFadingVolume()) {
                        if (volume == 0) {
                            return Observable.just(0);
                        }

                        final int steps = volume;
                        final long millisLeft = sleepTimer.getMillisLeft();
                        final long millisPerStep = millisLeft / steps;

                        return Observable.intervalRange(1, steps, millisPerStep, millisPerStep, TimeUnit.MILLISECONDS)
                                .map(inverseVolume -> volume - inverseVolume.intValue());
                    }

                    return Observable.empty();
                });

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.disposable.add(this.fadeObservable
                .subscribe(this.mediaVolumeManager::setVolume));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.disposable.dispose();
    }
}
