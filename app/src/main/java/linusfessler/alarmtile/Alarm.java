package linusfessler.alarmtile;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Alarm {

    private static final int REQUEST_CODE = 0;

    private Context context;
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;

    private PendingIntent pendingIntent;
    private PendingIntent showIntent;

    public Alarm(Context context) {
        this.context = context;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void schedule(int delay) {
        setDnd(true);

        pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, new Intent(context, AlarmActivity.class), PendingIntent.FLAG_ONE_SHOT);
        showIntent = PendingIntent.getActivity(context, REQUEST_CODE + 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        if (alarmManager != null) {
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
        setDnd(false);

        if (alarmManager != null) {
            if (pendingIntent == null) {
                pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, new Intent(context, AlarmActivity.class), PendingIntent.FLAG_NO_CREATE);
            }
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }

            if (showIntent == null) {
                showIntent = PendingIntent.getActivity(context, REQUEST_CODE + 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_NO_CREATE);
            }
            if (showIntent != null) {
                showIntent.cancel();
            }
        }
    }

    private void setDnd(boolean enable) {
        boolean dndEnter = preferences.getBoolean("dnd_enter", false);
        if (!dndEnter) {
            return;
        }

        if (!PermissionUtility.isNotificationPolicyAccessGranted(context)) {
            return;
        }

        if (notificationManager != null) {
            if (enable) {
                boolean dndPriority = preferences.getBoolean("dnd_priority", false);
                if (dndPriority) {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                } else {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
                }
            } else {
                boolean dndExit = preferences.getBoolean("dnd_exit", false);
                if (dndExit) {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                }
            }
        }
    }
}
