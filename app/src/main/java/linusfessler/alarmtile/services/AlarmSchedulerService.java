package linusfessler.alarmtile.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import linusfessler.alarmtile.AlarmScheduler;
import linusfessler.alarmtile.DoNotDisturb;
import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.constants.PreferenceKeys;

@RequiresApi(24)
public class AlarmSchedulerService extends Service {

    private SharedPreferences preferences;

    private BroadcastReceiver alarmSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int alarmDelay = preferences.getInt(PreferenceKeys.SLEEP_LENGTH, 0);
            scheduleAlarm(alarmDelay);
        }
    };

    private BroadcastReceiver alarmDelayChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean alarmSet = preferences.getBoolean(PreferenceKeys.ALARM_SET, false);
            if (alarmSet) {
                int alarmDelay = preferences.getInt(PreferenceKeys.SLEEP_LENGTH, 0);
                scheduleAlarm(alarmDelay);
            }
        }
    };

    private BroadcastReceiver snoozeSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_LENGTH, 0);
            scheduleSnooze(snoozeDelay);
        }
    };

    private BroadcastReceiver snoozeDelayChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean snoozeSet = preferences.getBoolean(PreferenceKeys.SNOOZE_SET, false);
            if (snoozeSet) {
                int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_LENGTH, 0);
                scheduleSnooze(snoozeDelay);
            }
        }
    };

    private BroadcastReceiver alarmSnoozeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int snoozeDelay = preferences.getInt(PreferenceKeys.SNOOZE_LENGTH, 0);
            scheduleSnooze(snoozeDelay);
        }
    };

    private BroadcastReceiver alarmDismissReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dismiss();
        }
    };

    private BroadcastReceiver dndEnterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AlarmScheduler.alarmIsScheduled()) {
                boolean dndEnter = preferences.getBoolean(PreferenceKeys.DND_ENTER, false);
                if (dndEnter) {
                    boolean dndPriority = preferences.getBoolean(PreferenceKeys.DND_PRIORITY, false);
                    DoNotDisturb.turnOn(context, dndPriority);
                } else {
                    boolean dndExit = preferences.getBoolean(PreferenceKeys.DND_EXIT, false);
                    if (dndExit) {
                        DoNotDisturb.turnOff(context);
                    }
                }
            }
        }
    };

    private BroadcastReceiver dndPriorityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AlarmScheduler.alarmIsScheduled()) {
                boolean dndEnter = preferences.getBoolean(PreferenceKeys.DND_ENTER, false);
                if (dndEnter) {
                    boolean dndPriority = preferences.getBoolean(PreferenceKeys.DND_PRIORITY, false);
                    DoNotDisturb.turnOn(context, dndPriority);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSetReceiver, new IntentFilter(BroadcastActions.ALARM_SET));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmDelayChangedReceiver, new IntentFilter(BroadcastActions.ALARM_DELAY_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeSetReceiver, new IntentFilter(BroadcastActions.SNOOZE_SET));
        LocalBroadcastManager.getInstance(this).registerReceiver(snoozeDelayChangedReceiver, new IntentFilter(BroadcastActions.SNOOZE_DELAY_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmSnoozeReceiver, new IntentFilter(BroadcastActions.ALARM_SNOOZE));
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmDismissReceiver, new IntentFilter(BroadcastActions.ALARM_DISMISS));
        LocalBroadcastManager.getInstance(this).registerReceiver(dndEnterReceiver, new IntentFilter(BroadcastActions.DND_ENTER_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(dndPriorityReceiver, new IntentFilter(BroadcastActions.DND_PRIORITY_CHANGED));

        resume();
    }

    @Override
    public void onDestroy() {
        save();
        super.onDestroy();
    }

    public void resume() {
        if (AlarmScheduler.alarmIsScheduled()) {
            boolean alarmSet = preferences.getBoolean(PreferenceKeys.ALARM_SET, false);
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

    public void save() {
        if (AlarmScheduler.alarmIsScheduled()) {
            updateDelayLeftAndTimestamp();
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
        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, true)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .putInt(PreferenceKeys.DELAY_LEFT, delay)
                .putLong(PreferenceKeys.TIMESTAMP, System.currentTimeMillis())
                .apply();

        AlarmScheduler.schedule(this, delay);
    }

    private void scheduleSnooze(int delay) {
        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, true)
                .putInt(PreferenceKeys.DELAY_LEFT, delay)
                .putLong(PreferenceKeys.TIMESTAMP, System.currentTimeMillis())
                .apply();

        AlarmScheduler.schedule(this, delay);
    }

    private void dismiss() {
        AlarmScheduler.dismiss(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
