package linusfessler.alarmtile.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import linusfessler.alarmtile.services.AlarmSchedulerService;

public class AlarmResumingActionsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())
                || Intent.ACTION_MY_PACKAGE_REPLACED.equalsIgnoreCase(intent.getAction())) {
            context.startService(new Intent(context, AlarmSchedulerService.class));
        }
    }

    public static void enable(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmResumingActionsReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void disable(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmResumingActionsReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
