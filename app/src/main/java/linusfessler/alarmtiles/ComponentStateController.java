package linusfessler.alarmtiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public class ComponentStateController {

    private final Context context;
    private final PackageManager packageManager;

    public ComponentStateController(final Context context) {
        this.context = context.getApplicationContext();
        this.packageManager = context.getPackageManager();
    }

    public void enable(final ComponentName componentName) {
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void disable(final ComponentName componentName) {
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void enable(final Class cls) {
        final ComponentName componentName = new ComponentName(context, cls);
        enable(componentName);
    }

    public void disable(final Class cls) {
        final ComponentName componentName = new ComponentName(context, cls);
        disable(componentName);
    }

    public void enable(final String cls) {
        final ComponentName componentName = new ComponentName(context, cls);
        enable(componentName);
    }

    public void disable(final String cls) {
        final ComponentName componentName = new ComponentName(context, cls);
        disable(componentName);
    }
}
