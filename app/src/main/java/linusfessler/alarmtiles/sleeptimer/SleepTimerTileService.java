package linusfessler.alarmtiles.sleeptimer;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.lifecycle.Observer;

public class SleepTimerTileService extends TileService implements Observer<SleepTimer> {

    private SleepTimerRepository repository;
    private SleepTimer sleepTimer;

    @Override
    public void onCreate() {
        super.onCreate();
        this.repository = new SleepTimerRepository(this.getApplication());
        this.repository.getSleepTimer().observeForever(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.repository.getSleepTimer().removeObserver(this);
    }

    @Override
    public void onChanged(final SleepTimer sleepTimer) {
        this.sleepTimer = sleepTimer;
        this.updateTile();
    }

    @Override
    public void onClick() {
        this.sleepTimer.setEnabled(!this.sleepTimer.isEnabled());
        this.repository.setSleepTimer(this.sleepTimer);
    }

    @Override
    public void onStartListening() {
        this.updateTile();
    }

    private void updateTile() {
        final int state;
        if (this.sleepTimer.isEnabled()) {
            state = Tile.STATE_ACTIVE;
        } else {
            state = Tile.STATE_INACTIVE;
        }

        final Tile tile = this.getQsTile();
        tile.setState(state);
        tile.updateTile();
    }
}
