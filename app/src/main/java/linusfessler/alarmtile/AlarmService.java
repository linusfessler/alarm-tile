package linusfessler.alarmtile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class AlarmService extends TileService {

    private Alarm alarm;
    private SharedPreferences preferences;

    private BroadcastReceiver alarmChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(AlarmService.class.getSimpleName(), "aölsdkjfaölskd");
            dismiss();
            //int alarmDelay = preferences.getInt("alarm_delay", 0);
            //schedule(alarmDelay);
        }
    };

    private BroadcastReceiver snoozeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismiss();
            int snoozeDelay = preferences.getInt("snooze_delay", 0);
            schedule(snoozeDelay);
        }
    };

    private BroadcastReceiver dismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismiss();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        alarm = new Alarm(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(alarmChangedReceiver, new IntentFilter(BroadcastActions.ALARM_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeReceiver, new IntentFilter(BroadcastActions.SNOOZED));
        LocalBroadcastManager.getInstance(this).registerReceiver(dismissReceiver, new IntentFilter(BroadcastActions.DISMISSED));
    }

    @Override
    public void onTileAdded() {
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile.getState() == Tile.STATE_INACTIVE) {
            int alarmDelay = preferences.getInt("alarm_delay", 0);
            schedule(alarmDelay);
        } else {
            dismiss();
        }
    }

    private void schedule(int delay) {
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();

        alarm.schedule(delay);
    }

    private void dismiss() {
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();

        alarm.dismiss();
    }
}
