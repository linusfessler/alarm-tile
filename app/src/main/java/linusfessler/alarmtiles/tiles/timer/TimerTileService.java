package linusfessler.alarmtiles.tiles.timer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

import javax.inject.Inject;

import linusfessler.alarmtiles.core.App;

public class TimerTileService extends TileService {

    @Inject
    TimerViewModelFactory viewModelFactory;

    private TimerViewModel viewModel;
    private Timer timer;

    private final Observer<Timer> timerObserver = value -> {
        timer = value;

        final int state;
        if (timer.isEnabled()) {
            state = Tile.STATE_ACTIVE;
        } else {
            state = Tile.STATE_INACTIVE;
        }

        final Tile tile = getQsTile();
        tile.setState(state);
        tile.updateTile();
    };

    private final Observer<String> tileLabelObserver = tileLabel -> {
        final Tile tile = getQsTile();
        tile.setLabel(tileLabel);
        tile.updateTile();
    };

    @Override
    public void onCreate() {
        super.onCreate();

        ((App) getApplicationContext())
                .getTimerComponent()
                .inject(this);

        viewModel = viewModelFactory.create(TimerViewModel.class);
    }


    @Override
    public void onClick() {
        if (timer != null) {
            viewModel.toggle(timer);
        }
    }

    @Override
    public void onStartListening() {
        viewModel.getTimer().observeForever(timerObserver);
        viewModel.getTileLabel().observeForever(tileLabelObserver);

    }

    @Override
    public void onStopListening() {
        viewModel.getTimer().removeObserver(timerObserver);
        viewModel.getTileLabel().removeObserver(tileLabelObserver);
    }
}
