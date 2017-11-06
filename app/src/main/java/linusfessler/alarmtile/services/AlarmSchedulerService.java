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

@RequiresApi
public class AlarmSchedulerService extends TileService {

    private AlarmScheduler alarmScheduler;
    private SharedPreferences preferences;

    private BroadcastReceiver alarmSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean("alarm_set", false)) {
                scheduleAlarm();
            } else {
                dismiss();
            }
        }
    };

    private BroadcastReceiver alarmDelayChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean("alarm_set", false)) {
                scheduleAlarm();
            }
        }
    };

    private BroadcastReceiver snoozeSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean("snooze_set", false)) {
                scheduleSnooze();
            } else {
                dismiss();
            }
        }
    };

    private BroadcastReceiver snoozeDelayChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean("snooze_set", false)) {
                scheduleSnooze();
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
            scheduleSnooze();
        }
    };

    private BroadcastReceiver alarmDismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismiss();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        alarmScheduler = new AlarmScheduler(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSetReceiver, new IntentFilter(BroadcastActions.ALARM_SET));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmDelayChangedReceiver, new IntentFilter(BroadcastActions.ALARM_DELAY_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeSetReceiver, new IntentFilter(BroadcastActions.SNOOZE_SET));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeDelayChangedReceiver, new IntentFilter(BroadcastActions.SNOOZE_DELAY_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmStartedReceiver, new IntentFilter(BroadcastActions.ALARM_STARTED));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSnoozeReceiver, new IntentFilter(BroadcastActions.ALARM_SNOOZED));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmDismissReceiver, new IntentFilter(BroadcastActions.ALARM_DISMISSED));
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
                scheduleAlarm();
            } else {
                dismiss();
            }
        }
    }

    private void scheduleAlarm() {
        setTileActive();

        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, true)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .apply();

        Intent intent = new Intent(BroadcastActions.UPDATE_SWITCHES);
        intent.putExtra(PreferenceKeys.ALARM_SET, true);
        intent.putExtra(PreferenceKeys.SNOOZE_SET, false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        int alarmDelay = preferences.getInt(PreferenceKeys.ALARM_DELAY, 0);
        alarmScheduler.schedule(alarmDelay);
    }

    private void scheduleSnooze() {
        setTileActive();

        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, true)
                .apply();

        Intent intent = new Intent(BroadcastActions.UPDATE_SWITCHES);
        intent.putExtra(PreferenceKeys.ALARM_SET, false);
        intent.putExtra(PreferenceKeys.SNOOZE_SET, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_DELAY, 0);
        alarmScheduler.schedule(snoozeDelay);
    }

    private void dismiss() {
        setTileInactive();

        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .apply();

        Intent intent = new Intent(BroadcastActions.UPDATE_SWITCHES);
        intent.putExtra(PreferenceKeys.ALARM_SET, false);
        intent.putExtra(PreferenceKeys.SNOOZE_SET, false);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        alarmScheduler.dismiss();
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
