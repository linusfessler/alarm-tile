package linusfessler.alarmtile;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import linusfessler.alarmtile.activities.AlarmActivity;
import linusfessler.alarmtile.activities.PreferenceActivity;
import linusfessler.alarmtile.constants.PreferenceKeys;
import linusfessler.alarmtile.receivers.AlarmResumingActionsReceiver;
import linusfessler.alarmtile.utility.Permissions;

public class AlarmScheduler {

    private static final int REQUEST_CODE = 0;

    private static AlarmScheduler instance;
    public static AlarmScheduler getInstance(Context context) {
        if (instance == null) {
            instance = new AlarmScheduler(context);
        }
        return instance;
    }

    private Context context;
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;

    private PendingIntent getPendingIntent(int flags) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, REQUEST_CODE, intent, flags);
    }

    private AlarmScheduler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void schedule(int delay) {
        AlarmResumingActionsReceiver.enable(context);

        cancelPendingIntent();
        setDnd(true);

        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent showIntent = PendingIntent.getActivity(context, REQUEST_CODE + 1, new Intent(context, PreferenceActivity.class), 0);

            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            System.currentTimeMillis() + delay,
                            showIntent
                    ),
                    pendingIntent
            );
        }
    }

    public void dismiss() {
        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .apply();

        AlarmResumingActionsReceiver.disable(context);

        cancelPendingIntent();
        setDnd(false);
    }

    private void cancelPendingIntent() {
        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }

    private void setDnd(boolean enable) {
        boolean dndEnter = preferences.getBoolean(PreferenceKeys.DND_ENTER, false);
        if (!dndEnter) {
            return;
        }

        if (!Permissions.isNotificationPolicyAccessGranted(context)) {
            return;
        }

        if (notificationManager != null) {
            if (enable) {
                boolean dndPriority = preferences.getBoolean(PreferenceKeys.DND_PRIORITY, false);
                if (dndPriority) {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                } else {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
                }
            } else {
                boolean dndExit = preferences.getBoolean(PreferenceKeys.DND_EXIT, false);
                if (dndExit) {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                }
            }
        }
    }
}
