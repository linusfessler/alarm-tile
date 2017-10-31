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

public class AlarmSchedulerService extends TileService {

    private AlarmScheduler alarmScheduler;
    private SharedPreferences preferences;

    private boolean isActive;

    private BroadcastReceiver alarmChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isActive) {
                int alarmDelay = preferences.getInt("alarm_delay", 0);
                alarmScheduler.reschedule(alarmDelay, false);
            }
        }
    };

    private BroadcastReceiver snoozeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isActive) {
                int snoozeDelay = preferences.getInt("snooze_delay", 0);
                alarmScheduler.reschedule(snoozeDelay, false);
            }
        }
    };

    private BroadcastReceiver startedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setUnavailable();
        }
    };

    private BroadcastReceiver snoozeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setActive();
            int snoozeDelay = preferences.getInt("snooze_delay", 0);
            alarmScheduler.reschedule(snoozeDelay, true);
        }
    };

    private BroadcastReceiver dismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setInactive();
            alarmScheduler.dismiss();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        alarmScheduler = new AlarmScheduler(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(alarmChangedReceiver, new IntentFilter(BroadcastActions.ALARM_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeChangedReceiver, new IntentFilter(BroadcastActions.SNOOZE_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(startedReceiver, new IntentFilter(BroadcastActions.STARTED));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeReceiver, new IntentFilter(BroadcastActions.SNOOZED));
        LocalBroadcastManager.getInstance(this).registerReceiver(dismissReceiver, new IntentFilter(BroadcastActions.DISMISSED));
    }

    @Override
    public void onTileAdded() {
        setInactive();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile == null) {
            return;
        }

        if (tile.getState() == Tile.STATE_INACTIVE) {
            setActive();
            int alarmDelay = preferences.getInt("alarm_delay", 0);
            alarmScheduler.schedule(alarmDelay);
        } else {
            setInactive();
            alarmScheduler.dismiss();
        }
    }

    private void setActive() {
        isActive = true;
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
    }

    private void setUnavailable() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.updateTile();
        }
    }

    private void setInactive() {
        isActive = false;
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }
}
