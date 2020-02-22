package linusfessler.alarmtiles.shared

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import linusfessler.alarmtiles.core.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmClockManager @Inject constructor(private val application: Application, private val alarmManager: AlarmManager) {
    private val alarmIntent = Intent(application, AlarmBroadcastReceiver::class.java)
    private val showIntent = Intent(application, MainActivity::class.java)

    fun setAlarm(triggerTimestamp: Long, requestCode: Int) {
        val showPendingIntent = PendingIntent.getActivity(application, requestCode, showIntent, 0)
        val alarmPendingIntent = PendingIntent.getBroadcast(application, requestCode, alarmIntent, 0)

        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTimestamp, showPendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, alarmPendingIntent)
    }

    fun cancelAlarm(requestCode: Int) {
        val alarmPendingIntent = PendingIntent.getBroadcast(application, requestCode, alarmIntent, 0)
        alarmManager.cancel(alarmPendingIntent)
    }
}