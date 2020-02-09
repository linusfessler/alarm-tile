package linusfessler.alarmtiles.alarm;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.core.App;

@Singleton
public class AlarmTileService extends TileService {

    @Inject
    AlarmViewModelFactory viewModelFactory;

    private AlarmViewModel viewModel;
    private Alarm alarm;

    private final Observer<Alarm> alarmObserver = value -> {
        alarm = value;

        final int state;
        if (alarm.isEnabled()) {
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
                .getAppComponent()
                .inject(this);
        viewModel = viewModelFactory.create(AlarmViewModel.class);
    }


    @Override
    public void onClick() {
        if (alarm != null) {
            viewModel.toggle(alarm);
        }
    }

    @Override
    public void onStartListening() {
        viewModel.getAlarm().observeForever(alarmObserver);
        viewModel.getTileLabel().observeForever(tileLabelObserver);

    }

    @Override
    public void onStopListening() {
        viewModel.getAlarm().removeObserver(alarmObserver);
        viewModel.getTileLabel().removeObserver(tileLabelObserver);
    }
}
