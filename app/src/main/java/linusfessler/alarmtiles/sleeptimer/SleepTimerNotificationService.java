package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.core.MainActivity;

/**
 * The purpose of this service is to show a sticky notification so the application can live on in the background.
 */
public class SleepTimerNotificationService extends LifecycleService {

    private static final String START_ACTION = "START_ACTION";
    private static final String STOP_ACTION = "STOP_ACTION";

    private static final String NOTIFICATION_CANCEL_ACTION = "NOTIFICATION_CANCEL_ACTION";
    private static final String NOTIFICATION_FINISH_ACTION = "NOTIFICATION_FINISH_ACTION";

    private static final String NOTIFICATION_CHANNEL_ID = SleepTimerNotificationService.class.getName();
    private static final int NOTIFICATION_ID = 1;

    public static void start(final Application application) {
        final Intent intent = new Intent(application, SleepTimerNotificationService.class)
                .setAction(SleepTimerNotificationService.START_ACTION);
        application.startService(intent);
    }

    public static void stop(final Application application) {
        final Intent intent = new Intent(application, SleepTimerNotificationService.class)
                .setAction(SleepTimerNotificationService.STOP_ACTION);
        application.startService(intent);
    }

    @Inject
    NotificationManager notificationManager;

    @Inject
    SleepTimerViewModel viewModel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) getApplicationContext())
                .getSleepTimerComponent()
                .inject(this);
    }

    @Override
    public int onStartCommand(@Nullable final Intent intent, final int flags,
                              final int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent == null || intent.getAction() == null) {
            return Service.START_REDELIVER_INTENT;
        }

        switch (intent.getAction()) {
            case SleepTimerNotificationService.START_ACTION:
                start();
                break;
            case SleepTimerNotificationService.STOP_ACTION:
                stop();
                break;
            case SleepTimerNotificationService.NOTIFICATION_CANCEL_ACTION:
                cancelFromNotification();
                break;
            case SleepTimerNotificationService.NOTIFICATION_FINISH_ACTION:
                finishFromNotification();
                break;
            default:
                break;
        }

        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull final Intent intent) {
        return super.onBind(intent);
    }

    private void start() {
        createNotificationChannel();
        startForeground(SleepTimerNotificationService.NOTIFICATION_ID, buildRunningNotification(""));

        disposable.add(viewModel.getTimeLeft()
                .subscribe(timeLeft -> notificationManager.notify(SleepTimerNotificationService.NOTIFICATION_ID, buildRunningNotification(timeLeft))));
    }

    private void stop() {
        disposable.clear();

        stopForeground(Service.STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    private void cancelFromNotification() {
        viewModel.dispatch(new SleepTimerEvent.Cancel());
    }

    private void finishFromNotification() {
        viewModel.dispatch(new SleepTimerEvent.Finish());
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    private Notification buildRunningNotification(final String subText) {
        final Intent contentIntent = new Intent(this, MainActivity.class);
        final PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, 0);

        final Intent cancelIntent = new Intent(this, SleepTimerNotificationService.class)
                .setAction(SleepTimerNotificationService.NOTIFICATION_CANCEL_ACTION);
        final PendingIntent cancelPendingIntent = PendingIntent.getService(this, 0, cancelIntent, 0);

        final Intent finishIntent = new Intent(this, SleepTimerNotificationService.class)
                .setAction(SleepTimerNotificationService.NOTIFICATION_FINISH_ACTION);
        final PendingIntent finishPendingIntent = PendingIntent.getService(this, 0, finishIntent, 0);

        return new NotificationCompat.Builder(this, SleepTimerNotificationService.NOTIFICATION_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getString(R.string.sleep_timer_running_notification_content_title))
                .setContentText(getString(R.string.sleep_timer_running_notification_content_text))
                .setContentIntent(contentPendingIntent)
                .addAction(R.drawable.ic_clear_24px, getString(R.string.sleep_timer_running_notification_action_cancel), cancelPendingIntent)
                .addAction(R.drawable.ic_check_24px, getString(R.string.sleep_timer_running_notification_action_finish), finishPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1))
                .setShowWhen(false)
                .setSubText(getString(R.string.sleep_timer_running_notification_sub_text, subText))
                .setColor(getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_music_off_24px)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    SleepTimerNotificationService.NOTIFICATION_CHANNEL_ID,
                    getString(R.string.sleep_timer),
                    NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
