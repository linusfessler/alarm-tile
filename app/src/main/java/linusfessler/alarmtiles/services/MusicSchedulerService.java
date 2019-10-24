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

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Schedulers;

public class MusicSchedulerService extends JobService implements AudioManager.OnAudioFocusChangeListener {

    private static final int TURN_MUSIC_OFF_JOB_ID = 1000;
    private static final int FADE_MUSIC_OUT_JOB_ID = 1001;
    private static final int NOTIFICATION_ID = 1002;
    private static final int CANCEL_REQUEST_CODE = 1003;
    private static final String CANCEL_INTENT_EXTRA_KEY = "cancel";

    private static AudioManager.OnAudioFocusChangeListener listener;
    private static int originalVolume = -1;
    private static long endTime = 0;

    public static void scheduleTurnMusicOff(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            return;
        }

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        final boolean showNotification = preferences.getBoolean(context.getString(R.string.pref_show_music_notification_key), false);
        if (showNotification) {
            showNotification(context);
        }

        final int delay = 60000 * preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
        final JobInfo jobInfo = new JobInfo.Builder(TURN_MUSIC_OFF_JOB_ID, new ComponentName(context, MusicSchedulerService.class))
                .setMinimumLatency(delay)
                .setOverrideDeadline(delay)
                .build();
        jobScheduler.schedule(jobInfo);

        final AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
    }

    public static void scheduleFadeMusicOut(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            if (endTime == 0) {
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                final int delay = 60000 * preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
                endTime = System.currentTimeMillis() + delay;
            }

            final AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            if (audioManager != null) {
                final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                final long timeLeft = endTime - System.currentTimeMillis();
                final long timeStep = timeLeft / Math.max(currentVolume, 1);
                final JobInfo jobInfo = new JobInfo.Builder(FADE_MUSIC_OUT_JOB_ID, new ComponentName(context, MusicSchedulerService.class))
                        .setMinimumLatency(timeStep)
                        .setOverrideDeadline(timeStep)
                        .build();
                jobScheduler.schedule(jobInfo);
            }
        }
    }

    private static void showNotification(final Context context) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        final Intent intent = new Intent(context, MusicSchedulerService.class);
        intent.putExtra(CANCEL_INTENT_EXTRA_KEY, true);
        final PendingIntent pendingIntent = PendingIntent.getService(context, CANCEL_REQUEST_CODE, intent, 0);

        final Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_music_note)
                .setColor(context.getColor(R.color.colorPrimary))
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(pendingIntent)
                .setContentTitle(context.getString(R.string.notification_turn_music_off_title))
                .setContentText(context.getString(R.string.notification_turn_music_off_content));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            final int delay = 60000 * preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
            builder.setWhen(System.currentTimeMillis() + delay)
                    .setUsesChronometer(true)
                    .setChronometerCountDown(true);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void cancel(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.cancelAll();
        }

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }

        if (originalVolume != -1) {
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                originalVolume = -1;
                if (listener != null) {
                    audioManager.abandonAudioFocus(listener);
                    listener = null;
                }
                endTime = 0;
            }
        }
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent.getBooleanExtra(CANCEL_INTENT_EXTRA_KEY, false)) {
            cancel(this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            return true;
        }

        if (!Schedulers.getInstance(this).isScheduled()) {
            jobScheduler.cancelAll();
            return true;
        }

        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return true;
        }

        if (jobParameters.getJobId() == TURN_MUSIC_OFF_JOB_ID) {
            listener = this;
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            cancel(this);
        } else if (jobParameters.getJobId() == FADE_MUSIC_OUT_JOB_ID) {
            final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0);
            scheduleFadeMusicOut(this);
        }

        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters jobParameters) {
        return true;
    }

    @Override
    public void onAudioFocusChange(final int i) {
    }
}
