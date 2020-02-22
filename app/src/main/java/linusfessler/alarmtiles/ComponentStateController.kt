package linusfessler.alarmtiles

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComponentStateController
@Inject
constructor(private val application: Application) {
    private val packageManager: PackageManager = application.packageManager

    fun enable(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    fun disable(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    fun enable(cls: Class<*>) {
        val componentName = ComponentName(application, cls)
        enable(componentName)
    }

    fun disable(cls: Class<*>) {
        val componentName = ComponentName(application, cls)
        disable(componentName)
    }

    fun enable(cls: String) {
        val componentName = ComponentName(application, cls)
        enable(componentName)
    }

    fun disable(cls: String) {
        val componentName = ComponentName(application, cls)
        disable(componentName)
    }
}