package linusfessler.alarmtiles;

import android.app.NotificationManager;
import android.content.Context;

import linusfessler.alarmtiles.schedulers.AlarmScheduler;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.Schedulers;
import linusfessler.alarmtiles.schedulers.SnoozeScheduler;
import linusfessler.alarmtiles.schedulers.TimerScheduler;
import linusfessler.alarmtiles.utility.Permissions;

public class DoNotDisturb {

    private static DoNotDisturb instance;
    public static DoNotDisturb getInstance(Context context) {
        if (instance == null) {
            instance = new DoNotDisturb(context);
        }
        return instance;
    }

    private Context context;

    private DoNotDisturb(Context context) {
        this.context = context.getApplicationContext();
    }

    public void turnOn(boolean priority) {
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

    public void turnOff() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!Permissions.isNotificationPolicyAccessGranted(context) || notificationManager == null) {
            return;
        }

        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }
}
