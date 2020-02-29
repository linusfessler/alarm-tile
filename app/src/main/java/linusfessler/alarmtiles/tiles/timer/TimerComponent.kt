package linusfessler.alarmtiles.tiles.timer

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.timertiles.tiles.timer.TimerModule
import javax.inject.Singleton

@Singleton
@Component(modules = [TimerModule::class])
interface TimerComponent {
    @Component.Builder
    interface Builder {
        fun timerModule(alarmModule: TimerModule): Builder
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): TimerComponent
    }

    fun inject(timerTileFragment: TimerTileFragment)
    fun inject(timerTileService: TimerTileService)
}