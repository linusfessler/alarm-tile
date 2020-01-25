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

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.AppComponent;
import linusfessler.alarmtiles.R;

public class SleepTimerService extends LifecycleService {

    private static final String START_ACTION = "START_ACTION";
    private static final String NOTIFICATION_CANCEL_ACTION = "NOTIFICATION_CANCEL_ACTION";
    private static final String STOP_ACTION = "STOP_ACTION";

    private static final String NOTIFICATION_CHANNEL_ID = SleepTimerService.class.getName();
    private static final int NOTIFICATION_ID = 1;

    static void start(final Application application) {
        final Intent intent = new Intent(application, SleepTimerService.class)
                .setAction(SleepTimerService.START_ACTION);
        application.startService(intent);
    }

    static void stop(final Application application) {
        final Intent intent = new Intent(application, SleepTimerService.class)
                .setAction(SleepTimerService.STOP_ACTION);
        application.startService(intent);
    }

    @Inject
    SleepTimerViewModelFactory viewModelFactory;

    @Inject
    AudioManager audioManager;

    @Inject
    NotificationManager notificationManager;

    @Inject
    SleepTimerRepository sleepTimerRepository;

    @Inject
    SleepTimerWorker sleepTimerWorker; // Declare here in order to have Dagger create the object

    private SleepTimerViewModel viewModel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();

        final AppComponent appComponent = ((App) this.getApplicationContext()).getAppComponent();
        final SleepTimerServiceComponent sleepTimerServiceComponent = appComponent.sleepTimerServiceComponent(new SleepTimerServiceModule(this));
        sleepTimerServiceComponent.inject(this);

        this.viewModel = this.viewModelFactory.create(SleepTimerViewModel.class);
    }

    @Override
    public int onStartCommand(@Nullable final Intent intent, final int flags,
                              final int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent == null || intent.getAction() == null) {
            return Service.START_REDELIVER_INTENT;
        }

        switch (intent.getAction()) {
            case SleepTimerService.START_ACTION:
                this.start();
                break;
            case SleepTimerService.NOTIFICATION_CANCEL_ACTION:
                this.cancel();
                break;
            case SleepTimerService.STOP_ACTION:
                this.stop();
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
        this.createNotificationChannel();
        this.startForeground(SleepTimerService.NOTIFICATION_ID, this.buildRunningNotification(""));

        this.disposable.add(this.viewModel.getTimeLeft().subscribe(timeLeft ->
                this.notificationManager.notify(SleepTimerService.NOTIFICATION_ID, this.buildRunningNotification(timeLeft))));
    }

    private void cancel() {
        this.viewModel.cancel();
    }

    private void stop() {
        this.disposable.clear();

        this.stopForeground(Service.STOP_FOREGROUND_REMOVE);
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }

    private Notification buildRunningNotification(final String subText) {
        final Intent cancelIntent = new Intent(this, SleepTimerService.class)
                .setAction(SleepTimerService.NOTIFICATION_CANCEL_ACTION);
        final PendingIntent cancelPendingIntent = PendingIntent.getService(this, 0, cancelIntent, 0);

        return new NotificationCompat.Builder(this, SleepTimerService.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(this.getString(R.string.sleep_timer_running_notification_content_title))
                .setContentText(this.getString(R.string.sleep_timer_running_notification_content_text))
                .setShowWhen(false)
                .setSubText(this.getString(R.string.sleep_timer_running_notification_sub_text, subText))
                .setColor(this.getColor(R.color.colorPrimary))
//                .setColorized(true)
                .setSmallIcon(R.drawable.ic_music_off_24px)
                .setContentIntent(cancelPendingIntent)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    SleepTimerService.NOTIFICATION_CHANNEL_ID,
                    this.getString(R.string.sleep_timer),
                    NotificationManager.IMPORTANCE_LOW);

            this.notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
