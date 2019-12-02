package linusfessler.alarmtiles.timer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

public class TimerTileService extends TileService {

    private TimerViewModel viewModel;
    private Timer timer;

    private final Observer<Timer> timerObserver = value -> {
        this.timer = value;

        final int state;
        if (this.timer.isEnabled()) {
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
        this.viewModel = new TimerViewModel(this.getApplication());
    }

    @Override
    public void onClick() {
        if (this.timer != null) {
            this.viewModel.toggle(this.timer);
        }
    }

    @Override
    public void onStartListening() {
        this.viewModel.getTimer().observeForever(this.timerObserver);
        this.viewModel.getTileLabel().observeForever(this.tileLabelObserver);

    }

    @Override
    public void onStopListening() {
        this.viewModel.getTimer().removeObserver(this.timerObserver);
        this.viewModel.getTileLabel().removeObserver(this.tileLabelObserver);
    }
}