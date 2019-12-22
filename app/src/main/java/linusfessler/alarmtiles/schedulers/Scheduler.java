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
import linusfessler.alarmtiles.sleeptimer.SleepTimerJobService;
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

        this.notifyTileService();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(context.getString(this.getIsScheduledKey()), true)
                .putInt(this.getDurationLeftKey(), duration)
                .putLong(this.getTimestampKey(), System.currentTimeMillis())
                .commit();

        this.cancelPendingIntent();
        this.enableResume();

        final boolean dndEnter = preferences.getBoolean(context.getString(R.string.pref_dnd_enter_key), false);
        if (dndEnter) {
            final boolean dndPriority = preferences.getBoolean(context.getString(R.string.pref_dnd_priority_key), false);
            DoNotDisturb.getInstance(context).turnOn(dndPriority);
        }

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            final PendingIntent pendingIntent = this.getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT);
            final PendingIntent showIntent = PendingIntent.getActivity(context, this.getShowRequestCode(), new Intent(context, SettingsActivity.class), 0);

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
//            SleepTimerJobService.start(context);

            final boolean fadeMusicOut = preferences.getBoolean(context.getString(R.string.pref_fade_music_out_key), false);
            if (fadeMusicOut) {
//                SleepTimerJobService.scheduleFadeMusicOut(context);
            }
        }
    }

    public boolean isScheduled() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        return preferences.getBoolean(this.context.getString(this.getIsScheduledKey()), false);
    }

    public void notifyAlarmIsActive() {
        this.alarmIsActive = true;
        this.notifyTileService();
    }

    public boolean alarmIsActive() {
        return this.alarmIsActive;
    }

    public void dismiss() {
        this.alarmIsActive = false;
        this.notifyTileService();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        preferences.edit().putBoolean(this.context.getString(this.getIsScheduledKey()), false).apply();

        this.cancelPendingIntent();
        this.disableResume();

        if (!Schedulers.getInstance(this.context).isScheduled()) {
            final boolean dndExit = preferences.getBoolean(this.context.getString(R.string.pref_dnd_exit_key), false);
            if (dndExit) {
                DoNotDisturb.getInstance(this.context).turnOff();
            }

            SleepTimerJobService.cancel(this.context);
        }
    }

    private void notifyTileService() {
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(new Intent(BroadcastActions.UPDATE_TILE));
    }

    public void resume() {
        if (this.isScheduled()) {
            this.updateDurationLeftAndTimestamp();
            final int durationLeft = this.getDurationLeft();
            if (durationLeft > 0) {
                this.schedule(this.context, durationLeft);
            } else {
                this.dismiss();

                // show "missed settings_alarm" notification
                final Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(System.currentTimeMillis() + durationLeft);

                final Intent intent = new Intent(this.context, MainActivity.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

                final Notification notification = new Notification.Builder(this.context)
                        .setSmallIcon(R.drawable.ic_alarm_24px)
                        .setColor(this.context.getColor(R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDeleteIntent(pendingIntent)
                        .setContentTitle(this.context.getString(R.string.notification_missed_alarm_title))
                        .setContentText(DateFormatSymbols.getInstance().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE))
                        .build();

                final NotificationManager notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(0, notification);
                }
            }
        } else {
            this.dismiss();
        }
    }

    private void enableResume() {
        Components.enable(this.context, AlarmResumeReceiver.class);
    }

    private void disableResume() {
        if (!Schedulers.getInstance(this.context).isScheduled()) {
            Components.disable(this.context, AlarmResumeReceiver.class);
        }
    }

    private void updateDurationLeftAndTimestamp() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        int durationLeft = preferences.getInt(this.getDurationLeftKey(), 0);
        final long timestamp = preferences.getLong(this.getTimestampKey(), 0);
        final long now = System.currentTimeMillis();
        durationLeft -= now - timestamp;
        preferences.edit()
                .putInt(this.getDurationLeftKey(), durationLeft)
                .putLong(this.getTimestampKey(), now)
                .apply();
    }

    public int getDurationLeft() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        return preferences.getInt(this.getDurationLeftKey(), 0);
    }

    private PendingIntent getPendingIntent(final int flags) {
        final Intent intent = new Intent(this.context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this.context, this.getActivityRequestCode(), intent, flags);
    }

    private void cancelPendingIntent() {
        final AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            final PendingIntent pendingIntent = this.getPendingIntent(PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }
}
