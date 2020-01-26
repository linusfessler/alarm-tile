package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class SleepTimerCancelService implements LifecycleObserver {

    private final Application application;
    private final Observable<SleepTimer> cancelObservable;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public SleepTimerCancelService(final Application application, final SleepTimerRepository repository, final Lifecycle lifecycle) {
        this.application = application;

        this.cancelObservable = repository.getSleepTimer()
                .switchMap(sleepTimer -> {
                    if (!sleepTimer.isEnabled() && sleepTimer.isCancelled()) {
                        return Observable.just(sleepTimer);
                    }

                    return Observable.empty();
                });

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.disposable.add(this.cancelObservable
                .subscribe(sleepTimer -> SleepTimerService.stop(this.application)));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.disposable.dispose();
    }
}
