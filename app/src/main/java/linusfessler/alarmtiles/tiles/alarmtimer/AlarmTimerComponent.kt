package linusfessler.alarmtiles.tiles.alarmtimer

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AlarmTimerModule::class])
interface AlarmTimerComponent {
    @Component.Builder
    interface Builder {
        fun alarmTimerModule(alarmTimerModule: AlarmTimerModule): Builder
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): AlarmTimerComponent
    }

    fun inject(alarmTimerTileFragment: AlarmTimerTileFragment)
    fun inject(alarmTimerTileService: AlarmTimerTileService)
}