package linusfessler.alarmtiles.sleeptimer;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import linusfessler.alarmtiles.MediaPlaybackManager;
import linusfessler.alarmtiles.MediaVolumeManager;
import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimer;

@SuppressLint("CheckResult")
@SuppressWarnings("ResultOfMethodCallIgnored")
public class SleepTimerViewModel extends ViewModel {

    private final Application application;
    private final SleepTimerRepository repository;
    private final MediaVolumeManager mediaVolumeManager;
    private final MediaPlaybackManager mediaPlaybackManager;

    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> timeLeftObservable;

    SleepTimerViewModel(final Application application, final SleepTimerRepository repository, final TimeFormatter timeFormatter,
                        final MediaVolumeManager mediaVolumeManager, final MediaPlaybackManager mediaPlaybackManager) {
        this.application = application;
        this.repository = repository;
        this.mediaVolumeManager = mediaVolumeManager;
        this.mediaPlaybackManager = mediaPlaybackManager;

        this.sleepTimerObservable = this.repository.getSleepTimer();

        this.timeLeftObservable = this.sleepTimerObservable.switchMap(sleepTimer -> {
            if (sleepTimer.getState() != SleepTimerState.RUNNING) {
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
                            timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS));
        });
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTimeLeft() {
        return this.timeLeftObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void onClick() {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    if (sleepTimer.getState() == SleepTimerState.RUNNING) {
                        this.cancel();
                    } else {
                        this.start();
                    }
                });
    }

    public void start() {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    SleepTimerNotificationService.start(this.application);

                    final int originalVolume = this.mediaVolumeManager.getVolume();
                    sleepTimer.start(originalVolume);
                    this.repository.update(sleepTimer);
                });
    }

    public void cancel() {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    if (sleepTimer.getConfig().isResettingVolume()) {
                        this.mediaVolumeManager.setVolume(sleepTimer.getOriginalVolume());
                    }

                    sleepTimer.cancel();
                    this.repository.update(sleepTimer);

                    SleepTimerNotificationService.stop(this.application);
                });
    }

    public void finish() {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    this.mediaPlaybackManager.stopMediaPlayback();

                    if (sleepTimer.getConfig().isResettingVolume()) {
                        this.mediaVolumeManager.setVolume(sleepTimer.getOriginalVolume());
                    } else if (sleepTimer.getConfig().isFadingVolume()) {
                        this.mediaVolumeManager.setVolume(0);
                    }

                    sleepTimer.finish();
                    this.repository.update(sleepTimer);

                    SleepTimerNotificationService.stop(this.application);
                });
    }

    public void setDuration(final long duration) {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    sleepTimer.getConfig().setDuration(duration);
                    this.repository.update(sleepTimer);

                    this.start();
                });
    }

    public void setTimeUnit(final TimeUnit timeUnit) {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    sleepTimer.getConfig().setTimeUnit(timeUnit);
                    this.repository.update(sleepTimer);

                    this.start();
                });
    }

    public void setFadingVolume(final boolean fadingVolume) {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    sleepTimer.getConfig().setFadingVolume(fadingVolume);
                    this.repository.update(sleepTimer);
                });
    }

    public void setResettingVolume(final boolean resettingVolume) {
        this.repository.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    sleepTimer.getConfig().setResettingVolume(resettingVolume);
                    this.repository.update(sleepTimer);
                });
    }
}
