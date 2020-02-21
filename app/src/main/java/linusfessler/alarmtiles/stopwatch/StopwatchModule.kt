package linusfessler.alarmtiles.stopwatch

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
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
}