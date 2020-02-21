package linusfessler.alarmtiles.stopwatch

import android.app.Application
import androidx.room.Room
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import dagger.Module
import dagger.Provides
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.SharedModule
import javax.inject.Singleton

@Module(includes = [SharedModule::class])
class StopwatchModule {
    @Provides
    @Singleton
    fun stopwatchDatabase(application: Application): StopwatchDatabase {
        return Room
                .databaseBuilder(application, StopwatchDatabase::class.java, "stopwatch-database")
                .build()
                .populate()
    }

    @Provides
    @Singleton
    fun mobiusLoop(application: Application, stopwatchEventHandler: StopwatchEventHandler, stopwatchEffectHandler: StopwatchEffectHandler): MobiusLoop<Stopwatch, StopwatchEvent, StopwatchEffect> {
        val mobiusLoop = Mobius
                .loop(stopwatchEventHandler, stopwatchEffectHandler)
                .logger(AndroidLogger.tag(application.getString(R.string.stopwatch)))
                .startFrom(Stopwatch())
        mobiusLoop.dispatchEvent(StopwatchEvent.Initialize())
        return mobiusLoop
    }
}