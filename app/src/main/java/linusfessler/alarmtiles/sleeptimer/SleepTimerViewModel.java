package linusfessler.alarmtiles.sleeptimer;

import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.disposables.Disposable;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import linusfessler.alarmtiles.shared.TimeFormatter;

/**
 * Wraps the Mobius loop and prepares the model for the view
 */
@Singleton
public class SleepTimerViewModel {

    private final MobiusLoop<SleepTimer, SleepTimerEvent, SleepTimerEffect> loop;
    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> timeLeftObservable;

    @Inject
    public SleepTimerViewModel(final MobiusLoop<SleepTimer, SleepTimerEvent, SleepTimerEffect> loop, final TimeFormatter timeFormatter) {
        this.loop = loop;

        sleepTimerObservable = Observable.create(emitter -> {
            final Disposable mobiusDisposable = loop
                    .observe(emitter::onNext);
            emitter.setCancellable(mobiusDisposable::dispose);
        });

        timeLeftObservable = sleepTimerObservable
                .switchMap(sleepTimer -> {
                    if (!sleepTimer.isEnabled()) {
                        return Observable.just("");
                    }

                    final long millisLeft = sleepTimer.getMillisLeft();
                    final long secondsLeft = (long) Math.ceil(millisLeft / 1000d);

                    return Observable
                            .intervalRange(0, secondsLeft, 0, 1, TimeUnit.SECONDS)
                            .map(zeroBasedSecondsPassed ->
                                    timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS));
                });
    }

    public Observable<SleepTimer> getSleepTimer() {
        return sleepTimerObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTimeLeft() {
        return timeLeftObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void dispatch(final SleepTimerEvent event) {
        loop.dispatchEvent(event);
    }
}
