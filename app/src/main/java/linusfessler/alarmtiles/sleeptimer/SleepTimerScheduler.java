package linusfessler.alarmtiles.sleeptimer;

import androidx.core.util.Pair;
import androidx.lifecycle.LifecycleObserver;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import linusfessler.alarmtiles.VolumeObserver;
import lombok.Getter;

@Getter
class SleepTimerScheduler implements LifecycleObserver {

    private final Observable<Integer> fadeObservable;
    private final Observable<SleepTimer> finishObservable;

    @Inject
    @SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable"})
    SleepTimerScheduler(final SleepTimerRepository repository, final VolumeObserver volumeObserver) {
        final Observable<SleepTimer> sleepTimerObservable = repository.getSleepTimer();

        final Observable<Integer> volumeObservable = sleepTimerObservable
                .switchMap(sleepTimer -> {
                    if (!sleepTimer.isEnabled()) {
                        return Observable.empty();
                    }
                    return volumeObserver.getObservable();
                });

        this.fadeObservable = Observable.combineLatest(sleepTimerObservable, volumeObservable, Pair::new)
                .switchMap(sleepTimerVolumePair -> {
                    final SleepTimer sleepTimer = sleepTimerVolumePair.first;
                    final int volume = sleepTimerVolumePair.second;

                    if (!sleepTimer.isEnabled() || volume == 0) {
                        return Observable.empty();
                    }

                    final int steps = volume;
                    final long millisLeft = sleepTimer.getMillisLeft();

                    final int start = 1;
                    final int count = steps - 1; // Count does not include the first step
                    final long millisPerStep = millisLeft / steps;

                    return Observable.intervalRange(start, count, millisPerStep, millisPerStep, TimeUnit.MILLISECONDS)
                            .map(inverseVolume -> volume - inverseVolume.intValue());
                });

        this.finishObservable = sleepTimerObservable
                .switchMap(sleepTimer -> {
                    if (!sleepTimer.isEnabled()) {
                        return Observable.just(sleepTimer);
                    }
                    return Observable.timer(sleepTimer.getMillisLeft(), TimeUnit.MILLISECONDS)
                            .map(zero -> sleepTimer);
                });
    }
}
