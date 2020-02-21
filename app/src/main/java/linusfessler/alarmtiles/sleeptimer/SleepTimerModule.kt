package linusfessler.alarmtiles.sleeptimer

import android.app.Application
import androidx.room.Room
import com.spotify.mobius.EventSource
import com.spotify.mobius.Mobius
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.rx2.RxEventSources
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.Initialize
import javax.inject.Singleton

@Module(includes = [SharedModule::class])
class SleepTimerModule {
    @Provides
    @Singleton
    fun sleepTimerDatabase(application: Application): SleepTimerDatabase {
        return Room
                .databaseBuilder(application, SleepTimerDatabase::class.java, "sleep-timer-database")
                .build()
                .populate()
    }

    @Provides
    @Singleton
    fun sleepTimerLoop(application: Application, sleepTimerEventHandler: SleepTimerEventHandler, sleepTimerEffectHandler: SleepTimerEffectHandler, volumeObservable: Observable<Int>): MobiusLoop<SleepTimer, SleepTimerEvent, SleepTimerEffect> {
        val volumeChangedEventObservable: Observable<SleepTimerEvent> = volumeObservable
                .map {
                    SleepTimerEvent.VolumeChanged(it)
                }

        val volumeEventSource: EventSource<SleepTimerEvent> = RxEventSources
                .fromObservables(volumeChangedEventObservable)

        val sleepTimerLoop = Mobius
                .loop(sleepTimerEventHandler, sleepTimerEffectHandler)
                .eventSource(volumeEventSource)
                .logger(AndroidLogger.tag(application.getString(R.string.sleep_timer)))
                .startFrom(SleepTimer())

        sleepTimerLoop.dispatchEvent(Initialize())

        return sleepTimerLoop
    }
}