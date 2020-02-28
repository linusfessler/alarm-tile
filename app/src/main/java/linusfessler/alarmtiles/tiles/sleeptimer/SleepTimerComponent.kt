package linusfessler.alarmtiles.tiles.sleeptimer

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import javax.inject.Singleton

@Singleton
@Component(modules = [SleepTimerModule::class])
interface SleepTimerComponent {
    @Component.Builder
    interface Builder {
        fun sleepTimerModule(sleepTimerModule: SleepTimerModule): Builder
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): SleepTimerComponent
    }

    fun inject(sleepTimerTileFragment: SleepTimerTileFragment)
    fun inject(sleepTimerConfigFragment: SleepTimerConfigFragment)
    fun inject(sleepTimerTileService: SleepTimerTileService)
    fun inject(sleepTimerNotificationService: SleepTimerNotificationService)
}