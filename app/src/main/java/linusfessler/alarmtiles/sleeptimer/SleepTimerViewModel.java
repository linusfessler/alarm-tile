package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.content.DialogInterface;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.dialogwrapper.AlertDialogWrapper;

@Singleton
public class SleepTimerViewModel extends ViewModel {

    private final Application application;
    private final SleepTimerRepository repository;
    private final TimeFormatter timeFormatter;

    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> timeLeftObservable;
    private final Observable<Boolean> configurableObservable;
    private final Observable<Boolean> resettingVolumeEnabledObservable;

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

        this.configurableObservable = this.sleepTimerObservable.switchMap(sleepTimer ->
                Observable.just(!sleepTimer.isEnabled()));

        this.resettingVolumeEnabledObservable = this.sleepTimerObservable.switchMap(sleepTimer ->
                Observable.just(sleepTimer.getConfig().isFading()));
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTimeLeft() {
        return this.timeLeftObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> isConfigurable() {
        return this.configurableObservable.observeOn(AndroidSchedulers.mainThread());
    }

    Observable<Boolean> isResettingVolumeEnabled() {
        return this.resettingVolumeEnabledObservable.observeOn(AndroidSchedulers.mainThread());
    }

    public void toggle(final SleepTimer sleepTimer, final AlertDialogWrapper alertDialogWrapper) {
        if (sleepTimer.isEnabled()) {
            SleepTimerService.cancel(this.application);
        } else {
            alertDialogWrapper.getDialog().setButton(DialogInterface.BUTTON_POSITIVE, this.application.getString(R.string.dialog_ok), (dialog, which) ->
                    SleepTimerService.start(this.application));
            alertDialogWrapper.showDialog();
        }
    }

    void setUnavailable(final SleepTimer sleepTimer, final boolean unavailable) {
        sleepTimer.setUnavailable(unavailable);
        this.repository.update(sleepTimer);
    }

    void setDuration(final SleepTimer sleepTimer, final int hours, final int minutes) {
        final long duration = (hours * 60 + minutes) * 60 * 1000L;
        sleepTimer.getConfig().setDuration(duration);
        this.repository.update(sleepTimer);
    }

    void setFading(final SleepTimer sleepTimer, final boolean fading) {
        sleepTimer.getConfig().setFading(fading);
        this.repository.update(sleepTimer);
    }

    void setResettingVolume(final SleepTimer sleepTimer, final boolean resettingVolume) {
        sleepTimer.getConfig().setResettingVolume(resettingVolume);
        this.repository.update(sleepTimer);
    }
}
