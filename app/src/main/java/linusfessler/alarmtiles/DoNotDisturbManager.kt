package linusfessler.alarmtiles

import android.app.NotificationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DoNotDisturbManager
@Inject
constructor(private val notificationManager: NotificationManager) {
    fun turnOn(priority: Boolean) {
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            return
        }

        if (priority) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
        }
    }

    fun turnOff() {
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            return
        }

        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }
}