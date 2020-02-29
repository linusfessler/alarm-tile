package linusfessler.alarmtiles.tiles.sleeptimer

import android.app.*
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.App
import linusfessler.alarmtiles.shared.MainActivity
import javax.inject.Inject

/**
 * The main purpose of this service is to show a sticky notification so the application can live on in the background.
 */
class SleepTimerNotificationService : LifecycleService() {
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var viewModel: SleepTimerViewModel

    private val disposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        (applicationContext as App)
                .sleepTimerComponent
                .inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null || intent.action == null) {
            return Service.START_REDELIVER_INTENT
        }

        when (intent.action) {
            START_ACTION -> start()
            STOP_ACTION -> stop()
            NOTIFICATION_CANCEL_ACTION -> cancelFromNotification()
            NOTIFICATION_FINISH_ACTION -> finishFromNotification()
        }

        return Service.START_REDELIVER_INTENT
    }

    private fun start() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildRunningNotification(""))
        disposable.add(viewModel.timeLeft
                .subscribe {
                    notificationManager.notify(NOTIFICATION_ID, buildRunningNotification(it))
                })
    }

    private fun stop() {
        disposable.clear()
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun cancelFromNotification() {
        viewModel.dispatch(SleepTimerEvent.Cancel())
    }

    private fun finishFromNotification() {
        viewModel.dispatch(SleepTimerEvent.Finish())
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun buildRunningNotification(subText: String): Notification {
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, 0)

        val cancelIntent = Intent(this, SleepTimerNotificationService::class.java)
                .setAction(NOTIFICATION_CANCEL_ACTION)
        val cancelPendingIntent = PendingIntent.getService(this, 0, cancelIntent, 0)

        val finishIntent = Intent(this, SleepTimerNotificationService::class.java)
                .setAction(NOTIFICATION_FINISH_ACTION)
        val finishPendingIntent = PendingIntent.getService(this, 0, finishIntent, 0)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getString(R.string.sleep_timer_running_notification_content_title))
                .setContentText(getString(R.string.sleep_timer_running_notification_content_text))
                .setContentIntent(contentPendingIntent)
                .addAction(R.drawable.ic_clear_24px, getString(R.string.sleep_timer_running_notification_action_cancel), cancelPendingIntent)
                .addAction(R.drawable.ic_check_24px, getString(R.string.sleep_timer_running_notification_action_finish), finishPendingIntent)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1))
                .setShowWhen(false)
                .setSubText(getString(R.string.sleep_timer_running_notification_sub_text, subText))
                .setColor(getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_music_off_24px)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    getString(R.string.sleep_timer),
                    NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val START_ACTION = "START_ACTION"
        private const val STOP_ACTION = "STOP_ACTION"

        private const val NOTIFICATION_CANCEL_ACTION = "NOTIFICATION_CANCEL_ACTION"
        private const val NOTIFICATION_FINISH_ACTION = "NOTIFICATION_FINISH_ACTION"

        private const val NOTIFICATION_CHANNEL_ID = "SleepTimerNotificationService"
        private const val NOTIFICATION_ID = 1

        fun start(application: Application) {
            val intent = Intent(application, SleepTimerNotificationService::class.java)
                    .setAction(START_ACTION)
            application.startService(intent)
        }

        fun stop(application: Application) {
            val intent = Intent(application, SleepTimerNotificationService::class.java)
                    .setAction(STOP_ACTION)
            application.startService(intent)
        }
    }
}