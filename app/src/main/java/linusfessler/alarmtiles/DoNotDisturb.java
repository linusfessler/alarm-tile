package linusfessler.alarmtiles;

import android.app.NotificationManager;
import android.content.Context;

import linusfessler.alarmtiles.utility.Permissions;

public class DoNotDisturb {

    private static DoNotDisturb instance;

    public static DoNotDisturb getInstance(final Context context) {
        if (instance == null) {
            instance = new DoNotDisturb(context);
        }
        return instance;
    }

    private final Context context;

    private DoNotDisturb(final Context context) {
        this.context = context.getApplicationContext();
    }

    public void turnOn(final boolean priority) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!Permissions.isNotificationPolicyAccessGranted(context) || notificationManager == null) {
            return;
        }

        if (priority) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
        }
    }

    public void turnOff() {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!Permissions.isNotificationPolicyAccessGranted(context) || notificationManager == null) {
            return;
        }

        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
