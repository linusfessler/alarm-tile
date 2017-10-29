package linusfessler.alarmtile;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import linusfessler.alarmtile.activity.AlarmActivity;
import linusfessler.alarmtile.activity.MainActivity;

public class AlarmScheduler {

    public static boolean snoozing = false;

    private static final int REQUEST_CODE = 0;

    private Context context;
    private AlarmManager alarmManager;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;

    private PendingIntent getPendingIntent(int flags) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, REQUEST_CODE, intent, flags);
    }

    public AlarmScheduler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void schedule(int delay) {
        setDnd(true);

        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent showIntent = PendingIntent.getActivity(context, REQUEST_CODE + 1, new Intent(context, MainActivity.class), 0);

            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            System.currentTimeMillis() + delay,
                            showIntent
                    ),
                    pendingIntent
            );
        }
    }

    public void reschedule(int delay, boolean snoozing) {
        AlarmScheduler.snoozing = snoozing;
        cancelPendingIntent();
        schedule(delay);
    }

    public void dismiss() {
        AlarmScheduler.snoozing = false;
        setDnd(false);
        cancelPendingIntent();
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
