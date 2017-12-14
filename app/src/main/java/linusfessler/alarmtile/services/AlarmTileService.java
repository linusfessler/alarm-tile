package linusfessler.alarmtile.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import linusfessler.alarmtile.AlarmScheduler;
import linusfessler.alarmtile.constants.BroadcastActions;

@RequiresApi(24)
public class AlarmTileService extends TileService {

    private BroadcastReceiver alarmScheduleChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AlarmScheduler.anyIsScheduled(context)) {
                setTileActive();
            } else {
                setTileInactive();
            }
        }
    };

    private BroadcastReceiver alarmStartedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTileUnavailable();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmScheduleChangedReceiver, new IntentFilter(BroadcastActions.ALARM_SCHEDULE_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmStartedReceiver, new IntentFilter(BroadcastActions.ALARM_STARTED));
    }

    @Override
    public void onTileAdded() {
        setTileInactive();
    }

    @Override
    public void onClick() {
        if (!AlarmScheduler.anyIsScheduled(this)) {
            setTileActive();
            AlarmScheduler.scheduleTimer(this);
        } else {
            setTileInactive();
            AlarmScheduler.dismiss(this);
        }
    }

    private void setTileActive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
    }

    private void setTileInactive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    private void setTileUnavailable() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.updateTile();
        }
    }
}
