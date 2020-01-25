package linusfessler.alarmtiles.sleeptimer;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import linusfessler.alarmtiles.TimeFormatter;

@SuppressLint("CheckResult")
@SuppressWarnings("ResultOfMethodCallIgnored")
public class SleepTimerViewModel extends ViewModel {

    private final Application application;
    private final SleepTimerRepository repository;
    private final TimeFormatter timeFormatter;

    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> timeLeftObservable;

    SleepTimerViewModel(final Application application, final SleepTimerRepository repository, final TimeFormatter timeFormatter) {
        this.application = application;
        this.repository = repository;
        this.timeFormatter = timeFormatter;

        // Don't emit null (only happens before database is populated at first app start)
        this.sleepTimerObservable = this.repository.getSleepTimer().switchMap(sleepTimer ->
                sleepTimer == null ? Observable.empty() : Observable.just(sleepTimer));

        this.timeLeftObservable = this.sleepTimerObservable.switchMap(sleepTimer -> {
            if (!sleepTimer.isEnabled()) {
                return Observable.just("");
            }

            final long millisLeft = sleepTimer.getMillisLeft();
            if (millisLeft < 0) {
                // Shouldn't happen but could potentially prevent a crash
                return Observable.just("");
            }

            final long secondsLeft = (long) Math.ceil(millisLeft / 1000.);
            return Observable.intervalRange(0, secondsLeft, 0, 1, TimeUnit.SECONDS)
                    .map(zeroBasedSecondsPassed ->
                            this.timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS));
        });
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTimeLeft() {
        return this.timeLeftObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void start(final long duration) {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    sleepTimer.start(duration);
                    this.repository.update(sleepTimer);

                    SleepTimerService.start(this.application);
                });
    }

    public void cancel() {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    sleepTimer.cancel();
                    this.repository.update(sleepTimer);

                    SleepTimerService.stop(this.application);
                });
    }
}
