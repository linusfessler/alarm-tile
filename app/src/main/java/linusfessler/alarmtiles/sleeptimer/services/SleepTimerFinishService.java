package linusfessler.alarmtiles.sleeptimer.services;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.sleeptimer.SleepTimerServiceScope;
import linusfessler.alarmtiles.sleeptimer.SleepTimerState;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModelFactory;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimer;

@SleepTimerServiceScope
public class SleepTimerFinishService implements LifecycleObserver {

    private final SleepTimerViewModel viewModel;
    private final Observable<SleepTimer> finishObservable;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public SleepTimerFinishService(final SleepTimerViewModelFactory viewModelFactory, final Lifecycle lifecycle) {
        this.viewModel = viewModelFactory.create(SleepTimerViewModel.class);

        this.finishObservable = this.viewModel.getSleepTimer()
                .switchMap(sleepTimer -> {
                    if (sleepTimer.getState() == SleepTimerState.RUNNING) {
                        return Observable.timer(sleepTimer.getMillisLeft(), TimeUnit.MILLISECONDS)
                                .map(zero -> sleepTimer);
                    }
                    return Observable.empty();
                });

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.disposable.add(this.finishObservable
                .subscribe(sleepTimer -> this.viewModel.finish()));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.disposable.dispose();
    }
}
