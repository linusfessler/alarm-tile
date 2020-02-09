package linusfessler.alarmtiles.stopwatch;

import android.annotation.SuppressLint;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import linusfessler.alarmtiles.shared.TimeFormatter;

@SuppressLint("CheckResult")
@SuppressWarnings("ResultOfMethodCallIgnored")
public class StopwatchViewModel extends ViewModel {

    private final StopwatchRepository repository;

    private final Observable<Boolean> enabledObservable;
    private final Observable<String> elapsedTimeObservable;

    public StopwatchViewModel(final StopwatchRepository repository, final TimeFormatter timeFormatter) {
        this.repository = repository;

        final Observable<Stopwatch> stopwatchObservable = this.repository.getStopwatch();

        enabledObservable = stopwatchObservable
                .map(Stopwatch::isEnabled);

        elapsedTimeObservable = stopwatchObservable.switchMap(stopwatch -> {
            if (!stopwatch.isEnabled()) {
                if (stopwatch.getStopTimestamp() == null) {
                    return Observable.just("");
                }

                final long elapsedMillis = stopwatch.getStopTimestamp() - stopwatch.getStartTimestamp();
                return Observable.just(timeFormatter.format(elapsedMillis, TimeUnit.MILLISECONDS));
            }

            final long elapsedMillisBase = System.currentTimeMillis() - stopwatch.getStartTimestamp();
            return Observable.interval(10, TimeUnit.MILLISECONDS)
                    .map(elapsedHundredthsOfASecond -> elapsedMillisBase + 10 * elapsedHundredthsOfASecond)
                    .map(elapsedMillis -> timeFormatter.format(elapsedMillis, TimeUnit.MILLISECONDS));
        });
    }

    public Observable<Boolean> isEnabled() {
        return enabledObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getElapsedTime() {
        return elapsedTimeObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void onClick() {
        repository.getStopwatch()
                .firstElement()
                .subscribe(stopwatch -> {
                    stopwatch.toggle();
                    repository.update(stopwatch);
                });
    }
}
