package linusfessler.alarmtiles.tiles.alarm

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
class AlarmModule {
    @Provides
    @Singleton
    fun alarmDatabase(application: Application): AlarmDatabase {
        return Room
                .databaseBuilder(application, AlarmDatabase::class.java, "alarm-database")
                .build()
                .populate()
    }

    @Provides
    @Singleton
    fun mobiusLoop(application: Application, alarmEventHandler: AlarmEventHandler, alarmEffectHandler: AlarmEffectHandler): MobiusLoop<Alarm, AlarmEvent, AlarmEffect> {
        val mobiusLoop = Mobius
                .loop(alarmEventHandler, alarmEffectHandler)
                .logger(AndroidLogger.tag(application.getString(R.string.alarm)))
                .startFrom(Alarm())
        mobiusLoop.dispatchEvent(AlarmEvent.Resume())
        return mobiusLoop
    }

    @Provides
    @Singleton
    fun alarmClockManager(alarmManager: AlarmManager, application: Application): AlarmClockManager<MainActivity, AlarmBroadcastReceiver> {
        return AlarmClockManager(application, alarmManager, MainActivity::class.java, AlarmBroadcastReceiver::class.java, ALARM_CLOCK_REQUEST_ID)
    }

    companion object {
        const val ALARM_CLOCK_REQUEST_ID = 768524
    }
}