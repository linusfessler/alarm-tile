package linusfessler.alarmtiles.sleeptimer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;

public class SleepTimerTileService extends TileService {

    @Inject
    SleepTimerViewModelFactory viewModelFactory;

    private SleepTimerViewModel viewModel;
    private SleepTimer sleepTimer;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) this.getApplicationContext()).getAppComponent().inject(this);
        this.viewModel = this.viewModelFactory.create(SleepTimerViewModel.class);
    }

    @Override
    public void onClick() {
        if (this.sleepTimer != null) {
            this.viewModel.toggle(this.sleepTimer);
        }
    }

    @Override
    public void onStartListening() {
        this.disposable.add(this.viewModel.getSleepTimer().subscribe(newSleepTimer -> {
            this.sleepTimer = newSleepTimer;

            final int state;
            if (this.sleepTimer.isEnabled()) {
                state = Tile.STATE_ACTIVE;
            } else {
                state = Tile.STATE_INACTIVE;
            }

            final Tile tile = this.getQsTile();
            tile.setState(state);
            tile.updateTile();
        }));

        this.disposable.add(this.viewModel.getTileLabel().subscribe(newTileLabel -> {
            final Tile tile = this.getQsTile();
            tile.setLabel(newTileLabel);
            tile.updateTile();
        }));

    }

    @Override
    public void onStopListening() {
        this.disposable.dispose();
    }
}
