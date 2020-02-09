package linusfessler.alarmtiles.sleeptimer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.shared.TileServiceCompat;

import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.toggle;

public class SleepTimerTileService extends TileService {

    @Inject
    SleepTimerViewModel viewModel;

    private String tileLabel;

    private final PublishSubject<Boolean> clickSubject = PublishSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) getApplicationContext())
                .getAppComponent()
                .inject(this);
        tileLabel = getString(R.string.sleep_timer);
    }

    @Override
    public void onClick() {
        super.onClick();
        clickSubject.onNext(true);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        disposable.add(viewModel.getSleepTimer()
                .subscribe(sleepTimer -> {
                    final int state;
                    if (sleepTimer.isEnabled()) {
                        state = Tile.STATE_ACTIVE;
                    } else {
                        state = Tile.STATE_INACTIVE;
                    }

                    final Tile tile = getQsTile();
                    tile.setState(state);
                    tile.updateTile();
                }));

        disposable.add(viewModel.getTimeLeft()
                .subscribe(timeLeft -> {
                    final Tile tile = getQsTile();
                    TileServiceCompat.setSubtitle(tile, tileLabel, timeLeft);
                    tile.updateTile();
                }));

        final Observable<SleepTimer> clickObservable = clickSubject
                .withLatestFrom(viewModel.getSleepTimer(), (click, sleepTimer) -> sleepTimer);

        disposable.add(clickObservable
                .subscribe(sleepTimer -> viewModel.dispatch(toggle())));
    }

    @Override
    public void onStopListening() {
        disposable.clear();
        super.onStopListening();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
