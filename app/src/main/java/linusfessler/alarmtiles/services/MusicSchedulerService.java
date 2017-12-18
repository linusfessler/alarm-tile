package linusfessler.alarmtiles.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.AlarmSchedulers;

public class MusicSchedulerService extends JobService implements AudioManager.OnAudioFocusChangeListener {

    private static final int TURN_MUSIC_OFF_JOB_ID = 1000;
    private static final int FADE_MUSIC_OUT_JOB_ID = 1001;
    private static final int NOTIFICATION_ID = 1002;
    private static final int CANCEL_REQUEST_CODE = 1003;
    private static final String CANCEL_INTENT_EXTRA_KEY = "cancel";

    private static int originalVolume = -1;

    public static void scheduleTurnMusicOff(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        Log.d("asdf", "0");
        if (jobScheduler == null) {
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean showNotification = preferences.getBoolean(context.getString(R.string.pref_show_music_notification_key), false);
        if (showNotification) {
            showNotification(context);
        }

        int delay = preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
        JobInfo jobInfo = new JobInfo.Builder(TURN_MUSIC_OFF_JOB_ID, new ComponentName(context, MusicSchedulerService.class))
                .setMinimumLatency(delay)
                .setOverrideDeadline(delay)
                .build();
        jobScheduler.schedule(jobInfo);

        Log.d("asdf", "1");
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.d("asdf", "2");
        }
    }

    public static void scheduleFadeMusicOut(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            int delay = preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
            AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            if (audioManager != null) {
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int timeStep = delay / (currentVolume + 2);
                JobInfo jobInfo = new JobInfo.Builder(FADE_MUSIC_OUT_JOB_ID, new ComponentName(context, MusicSchedulerService.class))
                        .setMinimumLatency(timeStep)
                        .setOverrideDeadline(timeStep)
                        .build();
                jobScheduler.schedule(jobInfo);
            }
        }
    }

    private static void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        Intent intent = new Intent(context, MusicSchedulerService.class);
        intent.putExtra(CANCEL_INTENT_EXTRA_KEY, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, CANCEL_REQUEST_CODE, intent, 0);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_music_note)
                .setColor(context.getColor(R.color.colorAccent))
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notification_turn_music_off_title))
                .setContentText(context.getString(R.string.notification_turn_music_off_content));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int delay = preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
            builder.setWhen(System.currentTimeMillis() + delay)
                    .setUsesChronometer(true)
                    .setChronometerCountDown(true);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void cancel(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.cancelAll();
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean(context.getString(R.string.pref_restore_volume_key), false)) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null && originalVolume != -1) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                originalVolume = -1;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(CANCEL_INTENT_EXTRA_KEY, false)) {
            cancel(this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            return true;
        }

        if (!AlarmSchedulers.isAnyScheduled(this)) {
            jobScheduler.cancelAll();
            return true;
        }

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return true;
        }

        if (jobParameters.getJobId() == TURN_MUSIC_OFF_JOB_ID) {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            cancel(this);
        } else if (jobParameters.getJobId() == FADE_MUSIC_OUT_JOB_ID) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0);
            scheduleFadeMusicOut(this);
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    @Override
    public void onAudioFocusChange(int i) {}
}
