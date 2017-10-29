package linusfessler.alarmtile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.v4.content.LocalBroadcastManager;

import linusfessler.alarmtile.AlarmScheduler;
import linusfessler.alarmtile.BroadcastActions;

public class AlarmTileService extends TileService {

    private AlarmScheduler alarmScheduler;
    private SharedPreferences preferences;

    private BroadcastReceiver alarmChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int alarmDelay = preferences.getInt("alarm_delay", 0);
            alarmScheduler.reschedule(alarmDelay, false);
        }
    };

    private BroadcastReceiver snoozeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int snoozeDelay = preferences.getInt("snooze_delay", 0);
            alarmScheduler.reschedule(snoozeDelay, true);
        }
    };

    private BroadcastReceiver dismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTileInactive();
            alarmScheduler.dismiss();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        alarmScheduler = new AlarmScheduler(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(alarmChangedReceiver, new IntentFilter(BroadcastActions.ALARM_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeReceiver, new IntentFilter(BroadcastActions.SNOOZED));
        LocalBroadcastManager.getInstance(this).registerReceiver(dismissReceiver, new IntentFilter(BroadcastActions.DISMISSED));
    }

    @Override
    public void onTileAdded() {
        setTileInactive();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile == null) {
            return;
        }

        if (tile.getState() == Tile.STATE_INACTIVE) {
            setTileActive();
            int alarmDelay = preferences.getInt("alarm_delay", 0);
            alarmScheduler.schedule(alarmDelay);
        } else {
            setTileInactive();
            alarmScheduler.dismiss();
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
}
