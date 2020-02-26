package linusfessler.alarmtiles.shared

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmClockManager<A : Activity, B : BroadcastReceiver> @Inject constructor(
        private val alarmManager: AlarmManager,
        application: Application,
        requestCode: Int,
        showActivity: Class<A>,
        alarmBroadcastReceiver: Class<B>
) {
    private val showIntent = Intent(application, showActivity)
    private val alarmIntent = Intent(application, alarmBroadcastReceiver)

    private val showPendingIntent = PendingIntent.getActivity(application, requestCode, showIntent, 0)
    private val alarmPendingIntent = PendingIntent.getBroadcast(application, requestCode, alarmIntent, 0)

    fun setAlarm(triggerTimestamp: Long) {
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTimestamp, showPendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, alarmPendingIntent)
    }

    fun cancelAlarm() {
        alarmManager.cancel(alarmPendingIntent)
    }
}