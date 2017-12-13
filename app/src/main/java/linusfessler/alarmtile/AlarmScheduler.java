package linusfessler.alarmtile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import linusfessler.alarmtile.activities.AlarmActivity;
import linusfessler.alarmtile.activities.MainActivity;
import linusfessler.alarmtile.constants.PreferenceKeys;
import linusfessler.alarmtile.receivers.AlarmResumingActionsReceiver;

public class AlarmScheduler {

    private static final int REQUEST_CODE = 0;
    private static boolean alarmIsScheduled = false;

    private AlarmScheduler() {}

    public static void schedule(Context context, int delay) {
        alarmIsScheduled = true;

        AlarmResumingActionsReceiver.enable(context);

        cancelPendingIntent(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean dndEnter = preferences.getBoolean(PreferenceKeys.DND_ENTER, false);
        if (dndEnter) {
            boolean dndPriority = preferences.getBoolean(PreferenceKeys.DND_PRIORITY, false);
            DoNotDisturb.turnOn(context, dndPriority);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(context, PendingIntent.FLAG_UPDATE_CURRENT);
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

    public static void dismiss(Context context) {
        alarmIsScheduled = false;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(PreferenceKeys.ALARM_SET, false)
                .putBoolean(PreferenceKeys.SNOOZE_SET, false)
                .apply();

        AlarmResumingActionsReceiver.disable(context);

        cancelPendingIntent(context);

        boolean dndExit = preferences.getBoolean(PreferenceKeys.DND_EXIT, false);
        if (dndExit) {
            DoNotDisturb.turnOff(context);
        }
    }

    public static boolean alarmIsScheduled() {
        return alarmIsScheduled;
    }

    private static PendingIntent getPendingIntent(Context context, int flags) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, REQUEST_CODE, intent, flags);
    }

    private static void cancelPendingIntent(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(context, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }
}
