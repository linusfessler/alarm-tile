package linusfessler.alarmtiles.timer

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import linusfessler.alarmtiles.shared.SharedModule
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
}