package linusfessler.alarmtiles.stopwatch;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

import javax.inject.Inject;

import linusfessler.alarmtiles.App;

public class StopwatchTileService extends TileService {

    @Inject
    StopwatchViewModelFactory viewModelFactory;

    private StopwatchViewModel viewModel;
    private Stopwatch stopwatch;

    private final Observer<Stopwatch> stopwatchObserver = value -> {
        this.stopwatch = value;

        final int state;
        if (this.stopwatch.isEnabled()) {
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
        ((App) this.getApplicationContext()).getAppComponent().inject(this);
        this.viewModel = this.viewModelFactory.create(StopwatchViewModel.class);
    }


    @Override
    public void onClick() {
        if (this.stopwatch != null) {
            this.viewModel.toggle(this.stopwatch);
        }
    }

    @Override
    public void onStartListening() {
        this.viewModel.getStopwatch().observeForever(this.stopwatchObserver);
        this.viewModel.getTileLabel().observeForever(this.tileLabelObserver);

    }

    @Override
    public void onStopListening() {
        this.viewModel.getStopwatch().removeObserver(this.stopwatchObserver);
        this.viewModel.getTileLabel().removeObserver(this.tileLabelObserver);
    }
}
