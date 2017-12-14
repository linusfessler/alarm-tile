package linusfessler.alarmtile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import linusfessler.alarmtile.activities.AlarmActivity;
import linusfessler.alarmtile.activities.MainActivity;
import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.receivers.AlarmResumeReceiver;

public class AlarmScheduler {

    private static final int REQUEST_CODE = 0;

    private AlarmScheduler() {}

    public static void scheduleTimer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int timerDuration = preferences.getInt(context.getString(R.string.key_timer_duration), 0);
        scheduleTimer(context, timerDuration);
    }

    public static void scheduleTimer(Context context, int timerDuration) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(context.getString(R.string.key_timer_set), true)
                .putBoolean(context.getString(R.string.key_snooze_set), false)
                .putInt(context.getString(R.string.key_duration_left), timerDuration)
                .putLong(context.getString(R.string.key_timestamp), System.currentTimeMillis())
                .apply();
        schedule(context, timerDuration);
    }

    public static void scheduleSnooze(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int snoozeDuration = preferences.getInt(context.getString(R.string.key_snooze_duration), 0);
        scheduleSnooze(context, snoozeDuration);
    }

    public static void scheduleSnooze(Context context, int snoozeDuration) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(context.getString(R.string.key_timer_set), false)
                .putBoolean(context.getString(R.string.key_snooze_set), true)
                .putInt(context.getString(R.string.key_duration_left), snoozeDuration)
                .putLong(context.getString(R.string.key_timestamp), System.currentTimeMillis())
                .apply();
        schedule(context, snoozeDuration);
    }

    private static void schedule(Context context, int duration) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.ALARM_SCHEDULE_CHANGED));
        prepareResume(context);

        cancelPendingIntent(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean dndEnter = preferences.getBoolean(context.getString(R.string.key_dnd_enter), false);
        if (dndEnter) {
            boolean dndPriority = preferences.getBoolean(context.getString(R.string.key_dnd_priority), false);
            DoNotDisturb.turnOn(context, dndPriority);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(context, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent showIntent = PendingIntent.getActivity(context, REQUEST_CODE + 1, new Intent(context, MainActivity.class), 0);

            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            System.currentTimeMillis() + duration,
                            showIntent
                    ),
                    pendingIntent
            );
        }
    }

    public static void dismiss(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(context.getString(R.string.key_timer_set), false)
                .putBoolean(context.getString(R.string.key_snooze_set), false)
                .apply();

        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.ALARM_SCHEDULE_CHANGED));
        stopResume(context);

        cancelPendingIntent(context);

        boolean dndExit = preferences.getBoolean(context.getString(R.string.key_dnd_exit), false);
        if (dndExit) {
            DoNotDisturb.turnOff(context);
        }
    }

    public static void resume(Context context) {
        if (anyIsScheduled(context)) {
            int durationLeft = updateDurationLeftAndTimestamp(context);
            if (timerIsScheduled(context)) {
                scheduleTimer(context, durationLeft);
            } else {
                scheduleSnooze(context, durationLeft);
            }
        } else {
            dismiss(context);
        }
    }

    private static void prepareResume(Context context) {
        AlarmResumeReceiver.enable(context);
        updateDurationLeftAndTimestamp(context);
    }

    private static void stopResume(Context context) {
        AlarmResumeReceiver.disable(context);
    }

    private static int updateDurationLeftAndTimestamp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int durationLeft = preferences.getInt(context.getString(R.string.key_duration_left), 0);
        long timestamp = preferences.getLong(context.getString(R.string.key_timestamp), 0);
        long now = System.currentTimeMillis();
        durationLeft -= now - timestamp;
        preferences.edit()
                .putInt(context.getString(R.string.key_duration_left), durationLeft)
                .putLong(context.getString(R.string.key_timestamp), now)
                .apply();
        return durationLeft;
    }

    public static boolean anyIsScheduled(Context context) {
        return timerIsScheduled(context) || snoozeIsScheduled(context);
    }

    public static boolean timerIsScheduled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.key_timer_set), false);
    }

    public static boolean snoozeIsScheduled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.key_snooze_set), false);
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
