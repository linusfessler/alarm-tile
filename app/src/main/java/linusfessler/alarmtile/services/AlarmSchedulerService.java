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
public class AlarmSchedulerService extends TileService {

    private AlarmScheduler alarmScheduler;
    private SharedPreferences preferences;

    private BroadcastReceiver alarmSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int alarmDelay = preferences.getInt(PreferenceKeys.ALARM_DELAY, 0);
            scheduleAlarm(alarmDelay);
        }
    };

    private BroadcastReceiver alarmDelayChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean("alarm_set", false)) {
                int alarmDelay = preferences.getInt(PreferenceKeys.ALARM_DELAY, 0);
                scheduleAlarm(alarmDelay);
            }
        }
    };

    private BroadcastReceiver snoozeSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_DELAY, 0);
            scheduleSnooze(snoozeDelay);
        }
    };

    private BroadcastReceiver snoozeDelayChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (preferences.getBoolean("snooze_set", false)) {
                int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_DELAY, 0);
                scheduleSnooze(snoozeDelay);
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
            int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_DELAY, 0);
            scheduleSnooze(snoozeDelay);
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
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmStartedReceiver, new IntentFilter(BroadcastActions.ALARM_START));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSnoozeReceiver, new IntentFilter(BroadcastActions.ALARM_SNOOZE));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmDismissReceiver, new IntentFilter(BroadcastActions.ALARM_DISMISS));
    }

    @Override
    public void onStartListening() {
        boolean alarmSet = preferences.getBoolean("alarm_set", false);
        boolean snoozeSet = preferences.getBoolean("snooze_set", false);

        if (alarmSet || snoozeSet) {
            int delayLeft = updateDelayLeftAndTimestamp();
            if (alarmSet) {
                scheduleAlarm(delayLeft);
            } else {
                scheduleSnooze(delayLeft);
            }
        } else {
            dismiss();
        }
    }

    @Override
    public void onStopListening() {
        boolean alarmSet = preferences.getBoolean("alarm_set", false);
        boolean snoozeSet = preferences.getBoolean("snooze_set", false);

        if (alarmSet || snoozeSet) {
            updateDelayLeftAndTimestamp();
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
                int alarmDelay = preferences.getInt(PreferenceKeys.ALARM_DELAY, 0);
                scheduleAlarm(alarmDelay);
            } else {
                dismiss();
            }
        }
    }

    private int updateDelayLeftAndTimestamp() {
        int delayLeft = preferences.getInt(PreferenceKeys.DELAY_LEFT, 0);
        long timestamp = preferences.getLong(PreferenceKeys.TIMESTAMP, 0);
        long now = System.currentTimeMillis();
        delayLeft -= now - timestamp;
        preferences.edit()
                .putInt(PreferenceKeys.DELAY_LEFT, delayLeft)
                .putLong(PreferenceKeys.TIMESTAMP, now)
                .apply();
        return delayLeft;
    }

    private void scheduleAlarm(int delay) {
        setTileActive();

        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, true)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .putInt(PreferenceKeys.DELAY_LEFT, delay)
                .putLong(PreferenceKeys.TIMESTAMP, System.currentTimeMillis())
                .apply();

        alarmScheduler.schedule(delay);
    }

    private void scheduleSnooze(int delay) {
        setTileActive();

        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, true)
                .putInt(PreferenceKeys.DELAY_LEFT, delay)
                .putLong(PreferenceKeys.TIMESTAMP, System.currentTimeMillis())
                .apply();

        alarmScheduler.schedule(delay);
    }

    private void dismiss() {
        setTileInactive();

        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .apply();

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
