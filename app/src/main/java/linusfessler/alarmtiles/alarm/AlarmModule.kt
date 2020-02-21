package linusfessler.alarmtiles.alarm

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import linusfessler.alarmtiles.shared.SharedModule
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
}