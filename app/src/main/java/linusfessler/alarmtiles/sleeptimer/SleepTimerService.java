package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.VolumeObserver;

@Singleton
public class SleepTimerService extends LifecycleService {

    private static final String START_ACTION = "START_ACTION";
    private static final String CANCEL_ACTION = "CANCEL_ACTION";
    private static final String ADD_1_MINUTE_ACTION = "ADD_1_MINUTE_ACTION";
    private static final String ADD_15_MINUTES_ACTION = "ADD_15_MINUTES_ACTION";
    private static final String FINISH_ACTION = "FINISH_ACTION";

    private static final String NOTIFICATION_CHANNEL_ID = SleepTimerService.class.getName();
    private static final int NOTIFICATION_ID = 1;

    static void start(final Application application) {
        final Intent intent = new Intent(application, SleepTimerService.class)
                .setAction(SleepTimerService.START_ACTION);
        application.startService(intent);
    }

    static void cancel(final Application application) {
        final Intent intent = new Intent(application, SleepTimerService.class)
                .setAction(SleepTimerService.CANCEL_ACTION);
        application.startService(intent);
    }

    static void finish(final Application application) {
        final Intent intent = new Intent(application, SleepTimerService.class)
                .setAction(SleepTimerService.FINISH_ACTION);
        application.startService(intent);
    }

    @Inject
    SleepTimerViewModelFactory viewModelFactory;

    @Inject
    AudioManager audioManager;

    @Inject
    NotificationManager notificationManager;

    @Inject
    SleepTimerWorker sleepTimerWorker;

    private SleepTimerViewModel viewModel;
    private VolumeObserver volumeObserver;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) this.getApplicationContext()).getAppComponent().inject(this);
        this.viewModel = this.viewModelFactory.create(SleepTimerViewModel.class);
        this.volumeObserver = new VolumeObserver(this, this.audioManager, this.getContentResolver());
    }

    @Override
    public int onStartCommand(@NonNull final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.getAction() == null) {
            return Service.START_REDELIVER_INTENT;
        }

        switch (intent.getAction()) {
            case SleepTimerService.START_ACTION:
                return this.start();
            case SleepTimerService.CANCEL_ACTION:
                return this.cancel();
            case SleepTimerService.FINISH_ACTION:
                return this.finish();
            case SleepTimerService.ADD_1_MINUTE_ACTION:
                return this.add1Minute();
            case SleepTimerService.ADD_15_MINUTES_ACTION:
                return this.add15Minutes();
            default:
                return Service.START_REDELIVER_INTENT;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull final Intent intent) {
        return super.onBind(intent);
    }

    private int start() {
        this.createNotificationChannel();
        this.startForeground(SleepTimerService.NOTIFICATION_ID, this.buildRunningNotification(""));
        this.sleepTimerWorker.start();

        this.disposable.add(this.volumeObserver.getObservable().subscribe(volume ->
                this.sleepTimerWorker.onVolumeChanged()));
        this.disposable.add(this.viewModel.getTimeLeft().subscribe(timeLeft ->
                this.notificationManager.notify(SleepTimerService.NOTIFICATION_ID, this.buildRunningNotification(timeLeft))));

        return Service.START_REDELIVER_INTENT;
    }

    private int cancel() {
        this.disposable.clear();

        this.sleepTimerWorker.cancel();

        this.stopForeground(Service.STOP_FOREGROUND_REMOVE);
        this.stopSelf();

        return Service.START_REDELIVER_INTENT;
    }

    private int finish() {
        this.disposable.clear();

        this.stopForeground(Service.STOP_FOREGROUND_REMOVE);
        this.stopSelf();

        return Service.START_REDELIVER_INTENT;
    }

    private int add1Minute() {
        this.sleepTimerWorker.add1Minute();
        return Service.START_REDELIVER_INTENT;
    }

    private int add15Minutes() {
        this.sleepTimerWorker.add15Minutes();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }

    private Notification buildRunningNotification(final String subText) {
        final Intent cancelIntent = new Intent(this, SleepTimerService.class)
                .setAction(SleepTimerService.CANCEL_ACTION);
        final PendingIntent cancelPendingIntent = PendingIntent.getService(this, 0, cancelIntent, 0);

        final Intent add1MinuteIntent = new Intent(this, SleepTimerService.class)
                .setAction(SleepTimerService.ADD_1_MINUTE_ACTION);
        final PendingIntent add1MinutePendingIntent = PendingIntent.getService(this, 0, add1MinuteIntent, 0);

        final Intent add15MinutesIntent = new Intent(this, SleepTimerService.class)
                .setAction(SleepTimerService.ADD_15_MINUTES_ACTION);
        final PendingIntent add15MinutesPendingIntent = PendingIntent.getService(this, 0, add15MinutesIntent, 0);

        return new NotificationCompat.Builder(this, SleepTimerService.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(this.getString(R.string.sleep_timer_running_notification_content_title))
                .setContentText(this.getString(R.string.sleep_timer_running_notification_content_text))
                .setShowWhen(false)
                .setSubText(this.getString(R.string.sleep_timer_running_notification_sub_text, subText))
                .setColor(this.getColor(R.color.colorPrimary))
                .setColorized(true)
                .setSmallIcon(R.drawable.ic_music_off_24px)
                .setContentIntent(cancelPendingIntent)
                .addAction(new NotificationCompat.Action(0, "+1m", add1MinutePendingIntent))
                .addAction(new NotificationCompat.Action(0, "+15m", add15MinutesPendingIntent))
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    SleepTimerService.NOTIFICATION_CHANNEL_ID,
                    this.getString(R.string.sleep_timer),
                    NotificationManager.IMPORTANCE_DEFAULT);

            this.notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
