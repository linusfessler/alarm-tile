package linusfessler.alarmtiles.tiles.alarmtimer

import android.app.AlarmManager
import android.app.Application
import androidx.room.Room
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import dagger.Module
import dagger.Provides
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.AlarmClockManager
import linusfessler.alarmtiles.shared.MainActivity
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.shared.alarmconfig.AlarmBroadcastReceiver
import javax.inject.Singleton

@Module(includes = [SharedModule::class])
class AlarmTimerModule {
    @Provides
    @Singleton
    fun alarmTimerDatabase(application: Application): AlarmTimerDatabase {
        return Room
                .databaseBuilder(application, AlarmTimerDatabase::class.java, "alarm-timer-database")
                .build()
                .populate()
    }

    @Provides
    @Singleton
    fun mobiusLoop(application: Application, alarmTimerEventHandler: AlarmTimerEventHandler, alarmTimerEffectHandler: AlarmTimerEffectHandler): MobiusLoop<AlarmTimer, AlarmTimerEvent, AlarmTimerEffect> {
        val mobiusLoop = Mobius
                .loop(alarmTimerEventHandler, alarmTimerEffectHandler)
                .logger(AndroidLogger.tag(application.getString(R.string.alarm_timer)))
                .startFrom(AlarmTimer())
        mobiusLoop.dispatchEvent(AlarmTimerEvent.Resume())
        return mobiusLoop
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