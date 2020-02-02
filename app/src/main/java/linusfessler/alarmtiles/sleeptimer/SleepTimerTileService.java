package linusfessler.alarmtiles.sleeptimer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TileServiceCompat;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimer;

@Singleton
public class SleepTimerTileService extends TileService {

    @Inject
    SleepTimerViewModelFactory viewModelFactory;

    @Inject
    InputMethodManager inputMethodManager;

    private SleepTimerViewModel viewModel;
    private String tileLabel;

    private final PublishSubject<Boolean> clickSubject = PublishSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) this.getApplicationContext()).getAppComponent().inject(this);

        this.viewModel = this.viewModelFactory.create(SleepTimerViewModel.class);
        this.tileLabel = this.getString(R.string.sleep_timer);
    }

    @Override
    public void onClick() {
        super.onClick();
        this.clickSubject.onNext(true);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        this.disposable.add(this.viewModel.getSleepTimer()
                .subscribe(sleepTimer -> {
                    final int state;
                    if (sleepTimer.getState() == SleepTimerState.RUNNING) {
                        state = Tile.STATE_ACTIVE;
                    } else {
                        state = Tile.STATE_INACTIVE;
                    }

                    final Tile tile = this.getQsTile();
                    tile.setState(state);
                    tile.updateTile();
                }));

        this.disposable.add(this.viewModel.getTimeLeft()
                .subscribe(timeLeft -> {
                    final Tile tile = this.getQsTile();
                    TileServiceCompat.setSubtitle(tile, this.tileLabel, timeLeft);
                    tile.updateTile();
                }));

        final Observable<SleepTimer> clickObservable = this.clickSubject.withLatestFrom(this.viewModel.getSleepTimer(), (click, sleepTimer) -> sleepTimer);

        this.disposable.add(clickObservable
                .subscribe(sleepTimer -> this.viewModel.onClick()));
    }

    @Override
    public void onStopListening() {
        this.disposable.clear();
        super.onStopListening();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
