package linusfessler.alarmtiles.alarm;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

public class AlarmTileService extends TileService {

    private AlarmViewModel viewModel;
    private Alarm alarm;

    private final Observer<Alarm> alarmObserver = value -> {
        this.alarm = value;

        final int state;
        if (this.alarm.isEnabled()) {
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
        this.viewModel = new AlarmViewModel(this.getApplication());
    }

    @Override
    public void onClick() {
        if (this.alarm != null) {
            this.viewModel.toggle(this.alarm);
        }
    }

    @Override
    public void onStartListening() {
        this.viewModel.getAlarm().observeForever(this.alarmObserver);
        this.viewModel.getTileLabel().observeForever(this.tileLabelObserver);

    }

    @Override
    public void onStopListening() {
        this.viewModel.getAlarm().removeObserver(this.alarmObserver);
        this.viewModel.getTileLabel().removeObserver(this.tileLabelObserver);
    }
}
