package linusfessler.alarmtiles.sleeptimer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

public class SleepTimerTileService extends TileService {

    private SleepTimerViewModel viewModel;
    private SleepTimer sleepTimer;

    private final Observer<SleepTimer> sleepTimerObserver = value -> {
        this.sleepTimer = value;

        final int state;
        if (this.sleepTimer.isEnabled()) {
            state = Tile.STATE_ACTIVE;
        } else {
            state = Tile.STATE_INACTIVE;
        }

        final Tile tile = this.getQsTile();
        tile.setState(state);
        tile.updateTile();
    };

    private final Observer<String> tileLabelObserver = tileLabel -> {
        final Tile tile = this.getQsTile();
        tile.setLabel(tileLabel);
        tile.updateTile();
    };

    @Override
    public void onCreate() {
        super.onCreate();
        this.viewModel = new SleepTimerViewModel(this.getApplication());
    }

    @Override
    public void onClick() {
        if (this.sleepTimer != null) {
            this.viewModel.toggleSleepTimer(this.sleepTimer);
        }
    }

    @Override
    public void onStartListening() {
        this.viewModel.getSleepTimer().observeForever(this.sleepTimerObserver);
        this.viewModel.getTileLabel().observeForever(this.tileLabelObserver);

    }

    @Override
    public void onStopListening() {
        this.viewModel.getSleepTimer().removeObserver(this.sleepTimerObserver);
        this.viewModel.getTileLabel().removeObserver(this.tileLabelObserver);
    }
}
