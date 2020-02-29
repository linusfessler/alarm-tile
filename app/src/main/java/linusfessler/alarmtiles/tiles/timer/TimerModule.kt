package linusfessler.timertiles.tiles.timer

import android.app.AlarmManager
import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import linusfessler.alarmtiles.shared.AlarmClockManager
import linusfessler.alarmtiles.shared.MainActivity
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.shared.alarmconfig.AlarmBroadcastReceiver
import linusfessler.alarmtiles.tiles.timer.TimerDatabase
import javax.inject.Singleton

@Module(includes = [SharedModule::class])
class TimerModule {
    @Provides
    @Singleton
    fun timerDatabase(application: Application): TimerDatabase {
        return Room
                .databaseBuilder(application, TimerDatabase::class.java, "timer-database")
                .build()
                .populate()
    }

    @Provides
    @Singleton
    fun alarmClockManager(alarmManager: AlarmManager, application: Application): AlarmClockManager<MainActivity, AlarmBroadcastReceiver> {
        return AlarmClockManager(application, alarmManager, MainActivity::class.java, AlarmBroadcastReceiver::class.java, ALARM_CLOCK_REQUEST_ID)
    }

    companion object {
        const val ALARM_CLOCK_REQUEST_ID = 865445
    }
}