package linusfessler.alarmtiles.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ResumeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_MY_PACKAGE_UNSUSPENDED -> {
                // Nothing to do, the application will resume any ongoing process
            }
        }
    }
}