package linusfessler.alarmtiles.schedulers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

import linusfessler.alarmtiles.DoNotDisturb;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.activities.AlarmActivity;
import linusfessler.alarmtiles.activities.MainActivity;
import linusfessler.alarmtiles.activities.SettingsActivity;
import linusfessler.alarmtiles.constants.BroadcastActions;
import linusfessler.alarmtiles.receivers.AlarmResumeReceiver;
import linusfessler.alarmtiles.services.MusicSchedulerService;
import linusfessler.alarmtiles.utility.Components;

public abstract class Scheduler {

    private boolean alarmIsActive = false;
    protected Context context;

    protected Scheduler(final Context context) {
        this.context = context.getApplicationContext();
    }

    protected abstract int getActivityRequestCode();

    protected abstract int getShowRequestCode();

    protected abstract int getIsScheduledKey();

    protected abstract String getDurationLeftKey();

    protected abstract String getTimestampKey();

    public abstract void schedule();

    protected void schedule(final Context context, final int duration) {
        Schedulers.getInstance(context).schedule();

        notifyTileService();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(context.getString(getIsScheduledKey()), true)
                .putInt(getDurationLeftKey(), duration)
                .putLong(getTimestampKey(), System.currentTimeMillis())
                .commit();

        cancelPendingIntent();
        enableResume();

        final boolean dndEnter = preferences.getBoolean(context.getString(R.string.pref_dnd_enter_key), false);
        if (dndEnter) {
            final boolean dndPriority = preferences.getBoolean(context.getString(R.string.pref_dnd_priority_key), false);
            DoNotDisturb.getInstance(context).turnOn(dndPriority);
        }

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            final PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT);
            final PendingIntent showIntent = PendingIntent.getActivity(context, getShowRequestCode(), new Intent(context, SettingsActivity.class), 0);

            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(
                            System.currentTimeMillis() + duration,
                            showIntent
                    ),
                    pendingIntent
            );
        }

        final boolean turnMusicOff = preferences.getBoolean(context.getString(R.string.pref_turn_music_off_key), false);
        if (turnMusicOff) {
            MusicSchedulerService.scheduleTurnMusicOff(context);

            final boolean fadeMusicOut = preferences.getBoolean(context.getString(R.string.pref_fade_music_out_key), false);
            if (fadeMusicOut) {
                MusicSchedulerService.scheduleFadeMusicOut(context);
            }
        }
    }

    public boolean isScheduled() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(getIsScheduledKey()), false);
    }

    public void notifyAlarmIsActive() {
        alarmIsActive = true;
        notifyTileService();
    }

    public boolean alarmIsActive() {
        return alarmIsActive;
    }

    public void dismiss() {
        alarmIsActive = false;
        notifyTileService();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(context.getString(getIsScheduledKey()), false).apply();

        cancelPendingIntent();
        disableResume();

        if (!Schedulers.getInstance(context).isScheduled()) {
            final boolean dndExit = preferences.getBoolean(context.getString(R.string.pref_dnd_exit_key), false);
            if (dndExit) {
                DoNotDisturb.getInstance(context).turnOff();
            }

            MusicSchedulerService.cancel(context);
        }
    }

    private void notifyTileService() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastActions.UPDATE_TILE));
    }

    public void resume() {
        if (isScheduled()) {
            updateDurationLeftAndTimestamp();
            final int durationLeft = getDurationLeft();
            if (durationLeft > 0) {
                schedule(context, durationLeft);
            } else {
                dismiss();

                // show "missed settings_alarm" notification
                final Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(System.currentTimeMillis() + durationLeft);

                final Intent intent = new Intent(context, MainActivity.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                final Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_alarm_24px)
                        .setColor(context.getColor(R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDeleteIntent(pendingIntent)
                        .setContentTitle(context.getString(R.string.notification_missed_alarm_title))
                        .setContentText(DateFormatSymbols.getInstance().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE))
                        .build();

                final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(0, notification);
                }
            }
        } else {
            dismiss();
        }
    }

    private void enableResume() {
        Components.enable(context, AlarmResumeReceiver.class);
    }

    private void disableResume() {
        if (!Schedulers.getInstance(context).isScheduled()) {
            Components.disable(context, AlarmResumeReceiver.class);
        }
    }

    private void updateDurationLeftAndTimestamp() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int durationLeft = preferences.getInt(getDurationLeftKey(), 0);
        final long timestamp = preferences.getLong(getTimestampKey(), 0);
        final long now = System.currentTimeMillis();
        durationLeft -= now - timestamp;
        preferences.edit()
                .putInt(getDurationLeftKey(), durationLeft)
                .putLong(getTimestampKey(), now)
                .apply();
    }

    public int getDurationLeft() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(getDurationLeftKey(), 0);
    }

    private PendingIntent getPendingIntent(final int flags) {
        final Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, getActivityRequestCode(), intent, flags);
    }

    private void cancelPendingIntent() {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            final PendingIntent pendingIntent = getPendingIntent(PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }
}
