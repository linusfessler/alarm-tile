package linusfessler.alarmtiles.services.tile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.constants.BroadcastActions;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.Schedulers;

@RequiresApi(24)
public abstract class TileService extends android.service.quicksettings.TileService {

    protected abstract boolean isActive();
    protected abstract void onEnable();
    protected abstract void onDisable();
    protected abstract int getEnabledIcon();
    protected abstract int getDisabledIcon();

    private BroadcastReceiver tileUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTile();
        }
    };

    @Override
    public void onCreate() {
        LocalBroadcastManager.getInstance(this).registerReceiver(tileUpdateReceiver, new IntentFilter(BroadcastActions.UPDATE_TILE));
    }

    @Override
    public void onTileAdded() {
        updateTile();
    }

    @Override
    public void onStartListening() {
        updateTile();
    }

    @Override
    public void onClick() {
        if (!isActive()) {
            setTileInactive();
            onEnable();
        } else {
            setTileActive();
            onDisable();
        }
    }

    protected boolean isUnavailable() {
        return Schedulers.getInstance(this).alarmIsActive();
    }

    private void updateTile() {
        if (isUnavailable()) {
            setTileUnavailable();
            return;
        }

        if (isActive()) {
            setTileActive();
        } else {
            setTileInactive();
        }
    }

    private void setTileActive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.setIcon(Icon.createWithResource(this, getEnabledIcon()));
            tile.updateTile();
        }
    }

    private void setTileInactive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_INACTIVE);
            tile.setIcon(Icon.createWithResource(this, getDisabledIcon()));
            tile.updateTile();
        }
    }

    private void setTileUnavailable() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setIcon(Icon.createWithResource(this, getDisabledIcon()));
            tile.updateTile();
        }
    }
}
