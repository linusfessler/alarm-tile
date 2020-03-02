package linusfessler.alarmtiles.tiles.alarm

import android.app.AlarmManager
import android.app.Application
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import dagger.Module
import dagger.Provides
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.AlarmBroadcastReceiver
import linusfessler.alarmtiles.shared.AlarmClockManager
import linusfessler.alarmtiles.shared.MainActivity
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.shared.alarm.*
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [SharedModule::class])
class AlarmModule {
    @Provides
    @Singleton
    fun mobiusLoop(application: Application, alarmEventHandler: AlarmEventHandler, alarmEffectHandler: AlarmEffectHandler): MobiusLoop<Alarm, AlarmEvent, AlarmEffect> {
        val mobiusLoop = Mobius
                .loop(alarmEventHandler, alarmEffectHandler)
                .logger(AndroidLogger.tag(application.getString(R.string.alarm)))
                .startFrom(Alarm(ALARM_ID))
        mobiusLoop.dispatchEvent(AlarmEvent.Resume())
        return mobiusLoop
    }

    @Provides
    @Named("alarmId")
    @Singleton
    fun alarmId(): Long = ALARM_ID

    @Provides
    @Singleton
    fun alarmClockManager(application: Application, alarmManager: AlarmManager): AlarmClockManager<MainActivity, AlarmBroadcastReceiver> =
            AlarmClockManager(application, alarmManager, MainActivity::class.java, AlarmBroadcastReceiver::class.java, ALARM_REQUEST_CODE)

    companion object {
        const val ALARM_ID = 0L
        const val ALARM_REQUEST_CODE = 0
    }
}