package linusfessler.alarmtiles;

import android.app.NotificationManager;
import android.content.Context;

import linusfessler.alarmtiles.utility.Permissions;

public class DoNotDisturb {

    private DoNotDisturb() {}

    public static void turnOn(Context context, boolean priority) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!Permissions.isNotificationPolicyAccessGranted(context) || notificationManager == null) {
            return;
        }

        if (priority) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
        }
    }

    public static void turnOff(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!Permissions.isNotificationPolicyAccessGranted(context) || notificationManager == null) {
            return;
        }

        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
