package linusfessler.alarmtiles.utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class Components {

    public static void enable(Context context, Class c) {
        ComponentName receiver = new ComponentName(context, c);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void disable(Context context, Class c) {
        ComponentName receiver = new ComponentName(context, c);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
