package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import linusfessler.alarmtiles.TimeFormatter;

@Singleton
public class SleepTimerViewModel extends ViewModel {

    private final Application application;
    private final SleepTimerRepository repository;
    private final String tileLabel;
    private final TimeFormatter timeFormatter;

    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> timeLeftObservable;
    private final Observable<String> tileLabelObservable;

    public SleepTimerViewModel(final Application application, final SleepTimerRepository repository, final String tileLabel, final TimeFormatter timeFormatter) {
        this.application = application;
        this.repository = repository;
        this.tileLabel = tileLabel;
        this.timeFormatter = timeFormatter;

        // Don't emit null (only happens before database is populated at first app start)
        this.sleepTimerObservable = this.repository.getSleepTimer().switchMap(sleepTimer ->
                sleepTimer == null ? Observable.empty() : Observable.just(sleepTimer));

        final Observable<String> timeLeftOptionalObservable = this.sleepTimerObservable.switchMap(sleepTimer -> {
            if (!sleepTimer.isEnabled()) {
                return Observable.just("");
            }

            final long millisElapsed = System.currentTimeMillis() - sleepTimer.getStartTimestamp();
            final long millisLeft = sleepTimer.getDuration() - millisElapsed;

            if (millisLeft < 0) {
                // Shouldn't happen but could potentially prevent a crash
                return Observable.just("");
            }

            final long secondsLeft = (long) Math.ceil(millisLeft / 1000.);
            return Observable.intervalRange(0, secondsLeft, 0, 1, TimeUnit.SECONDS)
                    .map(zeroBasedSecondsPassed ->
                            this.timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS));
        });

        // Don't emit anything when sleep timer is disabled
        this.timeLeftObservable = timeLeftOptionalObservable.switchMap(timeLeft ->
                timeLeft.equals("") ? Observable.empty() : Observable.just(timeLeft));

        this.tileLabelObservable = timeLeftOptionalObservable.switchMap(timeLeft ->
                Observable.just(this.tileLabel + "\n" + timeLeft));
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTimeLeft() {
        return this.timeLeftObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTileLabel() {
        return this.tileLabelObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void onClick(final SleepTimer sleepTimer) {
        if (sleepTimer.isEnabled()) {
            SleepTimerService.cancel(this.application);
        } else {
            SleepTimerService.start(this.application);
        }
    }
}
