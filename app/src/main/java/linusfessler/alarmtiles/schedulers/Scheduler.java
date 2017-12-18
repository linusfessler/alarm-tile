package linusfessler.alarmtiles.schedulers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

import linusfessler.alarmtiles.DoNotDisturb;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.activities.AlarmActivity;
import linusfessler.alarmtiles.activities.SettingsActivity;
import linusfessler.alarmtiles.constants.BroadcastActions;
import linusfessler.alarmtiles.receivers.AlarmResumeReceiver;
import linusfessler.alarmtiles.services.MusicSchedulerService;
import linusfessler.alarmtiles.utility.Components;

public abstract class Scheduler {

    private boolean alarmIsActive = false;

    protected Scheduler() {}

    protected abstract int getActivityRequestCode();
    protected abstract int getShowRequestCode();
    protected abstract int getIsScheduledKey();
    protected abstract String getDurationLeftKey();
    protected abstract String getTimestampKey();

    public abstract void schedule(Context context);

    protected void schedule(Context context, int duration) {
        notifyTileService(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(context.getString(getIsScheduledKey()), true)
                .putInt(getDurationLeftKey(), duration)
                .putLong(getTimestampKey(), System.currentTimeMillis())
                .commit();

        cancelPendingIntent(context);
        enableResume(context);

        if (AlarmSchedulers.isAnyScheduled(context)) {
            boolean dndEnter = preferences.getBoolean(context.getString(R.string.pref_dnd_enter_key), false);
            if (dndEnter) {
                boolean dndPriority = preferences.getBoolean(context.getString(R.string.pref_dnd_priority_key), false);
                DoNotDisturb.turnOn(context, dndPriority);
            }
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = getPendingIntent(context, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent showIntent = PendingIntent.getActivity(context, getShowRequestCode(), new Intent(context, SettingsActivity.class), 0);

            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            System.currentTimeMillis() + duration,
                            showIntent
                    ),
                    pendingIntent
            );
        }

        boolean turnMusicOff = preferences.getBoolean(context.getString(R.string.pref_turn_music_off_key), false);
        if (turnMusicOff) {
            MusicSchedulerService.scheduleTurnMusicOff(context);

            boolean fadeMusicOut = preferences.getBoolean(context.getString(R.string.pref_fade_music_out_key), false);
            if (fadeMusicOut) {
                MusicSchedulerService.scheduleFadeMusicOut(context);
            }
        }
    }

    public boolean isScheduled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(getIsScheduledKey()), false);
    }

    public void notifyAlarmIsActive(Context context) {
        alarmIsActive = true;
        notifyTileService(context);
    }

    public boolean alarmIsActive() {
        return alarmIsActive;
    }

    public void dismiss(Context context) {
        alarmIsActive = false;
        notifyTileService(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(context.getString(getIsScheduledKey()), false).commit();

        cancelPendingIntent(context);
        disableResume(context);

        if (!AlarmSchedulers.isAnyScheduled(context)) {
            boolean dndExit = preferences.getBoolean(context.getString(R.string.pref_dnd_exit_key), false);
            if (dndExit) {
                DoNotDisturb.turnOff(context);
            }

            MusicSchedulerService.cancel(context);
        }
    }

    private void notifyTileService(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.UPDATE_TILE));
    }

    public void resume(Context context) {
        if (isScheduled(context)) {
            updateDurationLeftAndTimestamp(context);
            int durationLeft = getDurationLeft(context);
            if (durationLeft > 0) {
                schedule(context, durationLeft);
            } else {
                dismiss(context);

                // show "missed alarm" notification
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(System.currentTimeMillis() + durationLeft);

                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_alarm_on)
                        .setColor(context.getColor(R.color.colorAccent))
                        .setContentTitle(context.getString(R.string.notification_missed_alarm_title))
                        .setContentText(DateFormatSymbols.getInstance().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE))
                        .build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(0, notification);
                }
            }
        } else {
            dismiss(context);
        }
    }

    private void enableResume(Context context) {
        Components.enable(context, AlarmResumeReceiver.class);
        updateDurationLeftAndTimestamp(context);
    }

    private void disableResume(Context context) {
        if (!AlarmSchedulers.isAnyScheduled(context)) {
            Components.disable(context, AlarmResumeReceiver.class);
        }
    }

    private void updateDurationLeftAndTimestamp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int durationLeft = preferences.getInt(getDurationLeftKey(), 0);
        long timestamp = preferences.getLong(getTimestampKey(), 0);
        long now = System.currentTimeMillis();
        durationLeft -= now - timestamp;
        preferences.edit()
                .putInt(getDurationLeftKey(), durationLeft)
                .putLong(getTimestampKey(), now)
                .apply();
    }

    public int getDurationLeft(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(getDurationLeftKey(), 0);
    }

    private PendingIntent getPendingIntent(Context context, int flags) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, getActivityRequestCode(), intent, flags);
    }

    private void cancelPendingIntent(Context context) {
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