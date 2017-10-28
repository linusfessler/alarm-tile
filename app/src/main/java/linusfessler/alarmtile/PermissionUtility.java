package linusfessler.alarmtile;

import android.app.NotificationManager;
import android.content.Context;

public class PermissionUtility {

    public static boolean isNotificationPolicyAccessGranted(final Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager != null && notificationManager.isNotificationPolicyAccessGranted();
    }
}
