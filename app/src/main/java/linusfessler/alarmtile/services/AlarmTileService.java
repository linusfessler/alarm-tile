package linusfessler.alarmtile.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import linusfessler.alarmtile.AlarmScheduler;
import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.constants.PreferenceKeys;

@RequiresApi(24)
public class AlarmTileService extends TileService {

    private SharedPreferences preferences;

    private BroadcastReceiver alarmSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean(PreferenceKeys.ALARM_SET, false)) {
                setTileActive();
            } else {
                setTileInactive();
            }
        }
    };

    private BroadcastReceiver snoozeSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean(PreferenceKeys.SNOOZE_SET, false)) {
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

    private BroadcastReceiver alarmSnoozeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTileActive();
        }
    };

    private BroadcastReceiver alarmDismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setTileInactive();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSetReceiver, new IntentFilter(BroadcastActions.ALARM_SET));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeSetReceiver, new IntentFilter(BroadcastActions.SNOOZE_SET));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmStartedReceiver, new IntentFilter(BroadcastActions.ALARM_START));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSnoozeReceiver, new IntentFilter(BroadcastActions.ALARM_SNOOZE));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmDismissReceiver, new IntentFilter(BroadcastActions.ALARM_DISMISS));
    }

    @Override
    public void onStartListening() {
        boolean alarmSet = preferences.getBoolean(PreferenceKeys.ALARM_SET, false);
        boolean snoozeSet = preferences.getBoolean(PreferenceKeys.SNOOZE_SET, false);

        if (alarmSet || snoozeSet) {
            setTileActive();
        } else {
            setTileInactive();
        }
    }

    @Override
    public void onTileAdded() {
        setTileInactive();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile != null) {
            if (tile.getState() == Tile.STATE_INACTIVE) {
                setTileActive();
                preferences.edit().putBoolean(PreferenceKeys.ALARM_SET, true).apply();
                int sleepLength = preferences.getInt(PreferenceKeys.SLEEP_LENGTH, 0);
                AlarmScheduler.getInstance(this).schedule(sleepLength);
            } else {
                setTileInactive();
                AlarmScheduler.getInstance(this).dismiss();
            }
        }
    }

    private void setTileActive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_ACTIVE);
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

    private void setTileInactive() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }
}
