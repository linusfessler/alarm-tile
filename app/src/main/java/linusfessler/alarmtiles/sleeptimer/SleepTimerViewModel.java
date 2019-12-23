package linusfessler.alarmtiles.sleeptimer;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.reactivex.Observable;
import linusfessler.alarmtiles.TimeFormatter;

@Singleton
public class SleepTimerViewModel extends ViewModel {

    private final SleepTimerService service;
    private final String tileLabel;
    private final TimeFormatter timeFormatter;

    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> tileLabelObservable;

    public SleepTimerViewModel(final SleepTimerService service, final String tileLabel, final TimeFormatter timeFormatter) {
        this.service = service;
        this.tileLabel = tileLabel;
        this.timeFormatter = timeFormatter;

        this.sleepTimerObservable = this.service.getSleepTimer();

        this.tileLabelObservable = this.sleepTimerObservable.switchMap(sleepTimer -> {
            if (sleepTimer == null || !sleepTimer.isEnabled()) {
                return Observable.just(this.tileLabel);
            }

            final long millisElapsed = System.currentTimeMillis() - sleepTimer.getStartTimestamp();
            final long millisLeft = sleepTimer.getDuration() - millisElapsed;
            final long secondsLeft = Math.round(millisLeft / 1000.);

            return Observable.intervalRange(0, secondsLeft, 0, 1, TimeUnit.SECONDS)
                    .map(zeroBasedSecondsPassed -> {
                        final String formattedTimeLeft = this.timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS);
                        return this.tileLabel + "\n" + formattedTimeLeft;
                    });
        });
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable;
    }

    public Observable<String> getTileLabel() {
        return this.tileLabelObservable;
    }

    public void onClick(final SleepTimer sleepTimer) {
        this.service.toggleSleepTimer(sleepTimer);
    }
}
