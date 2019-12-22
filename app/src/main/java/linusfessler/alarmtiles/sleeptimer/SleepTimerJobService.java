package linusfessler.alarmtiles.sleeptimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.SystemServiceNotAvailableException;
import linusfessler.alarmtiles.schedulers.Schedulers;

public class SleepTimerJobService extends JobService {

    public static final int JOB_ID = 1001;
    private static final int NOTIFICATION_ID = 1002;
    private static final int CANCEL_REQUEST_CODE = 1003;
    private static final String CANCEL_INTENT_EXTRA_KEY = "cancel";

    private static final int originalVolume = -1;
    private static final long endTime = 0;

    private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(final int focusChange) {

        }
    };

    public static void start(final Context context, final SleepTimerRepository sleepTimerRepository) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            throw new SystemServiceNotAvailableException(JobScheduler.class);
        }

//        if (showNotification) {
//            SleepTimerJobService.showNotification(context);
//        }


//        final JobInfo jobInfo = new JobInfo.Builder(SleepTimerJobService.JOB_ID, new ComponentName(context, SleepTimerJobService.class))
//                .setPeriodic(0, 0)
//                .setMinimumLatency(delay)
//                .setOverrideDeadline(delay)
//                .build();
//
//        jobScheduler.schedule(jobInfo);

//        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        if (audioManager != null) {
//            SleepTimerJobService.originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        }
    }

    public static void cancel(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            throw new SystemServiceNotAvailableException(JobScheduler.class);
        }

        jobScheduler.cancel(SleepTimerJobService.JOB_ID);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(SleepTimerJobService.NOTIFICATION_ID);
        }

//        if (SleepTimerJobService.originalVolume != -1) {
//            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//            if (audioManager != null) {
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, SleepTimerJobService.originalVolume, 0);
//                SleepTimerJobService.originalVolume = -1;
//                if (SleepTimerJobService.listener != null) {
//                    audioManager.abandonAudioFocus(SleepTimerJobService.listener);
//                    SleepTimerJobService.listener = null;
//                }
//                SleepTimerJobService.endTime = 0;
//            }
//        }
    }

//    public static void scheduleFadeMusicOut(final Context context) {
//        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        if (jobScheduler != null) {
//            if (SleepTimerJobService.endTime == 0) {
//                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                final int delay = 60000 * preferences.getInt(context.getString(R.string.pref_turn_music_off_delay_key), 0);
//                SleepTimerJobService.endTime = System.currentTimeMillis() + delay;
//            }
//
//            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//            if (audioManager != null) {
//                final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                final long timeLeft = SleepTimerJobService.endTime - System.currentTimeMillis();
//                final long timeStep = timeLeft / Math.max(currentVolume, 1);
//                final JobInfo jobInfo = new JobInfo.Builder(SleepTimerJobService.FADE_MUSIC_OUT_JOB_ID, new ComponentName(context, SleepTimerJobService.class))
//                        .setMinimumLatency(timeStep)
//                        .setOverrideDeadline(timeStep)
//                        .build();
//                jobScheduler.schedule(jobInfo);
//            }
//        }
//    }

    private static void showNotification(final Context context) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        final Intent intent = new Intent(context, SleepTimerJobService.class);
        intent.putExtra(SleepTimerJobService.CANCEL_INTENT_EXTRA_KEY, true);
        final PendingIntent pendingIntent = PendingIntent.getService(context, SleepTimerJobService.CANCEL_REQUEST_CODE, intent, 0);

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

        notificationManager.notify(SleepTimerJobService.NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent.getBooleanExtra(SleepTimerJobService.CANCEL_INTENT_EXTRA_KEY, false)) {
            SleepTimerJobService.cancel(this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        final JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            return true;
        }

        if (!Schedulers.getInstance(this).isScheduled()) {
            jobScheduler.cancelAll();
            return true;
        }

        final AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            return true;
        }

//        if (jobParameters.getJobId() == SleepTimerJobService.JOB_ID) {
//            SleepTimerJobService.listener = this;
//            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//            SleepTimerJobService.cancel(this);
//        } else if (jobParameters.getJobId() == SleepTimerJobService.FADE_MUSIC_OUT_JOB_ID) {
//            final int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0);
//            SleepTimerJobService.scheduleFadeMusicOut(this);
//        }

        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters jobParameters) {
        return true;
    }
}
