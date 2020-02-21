package linusfessler.alarmtiles.timer

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import javax.inject.Singleton

@Singleton
@Component(modules = [TimerModule::class])
interface TimerComponent {
    @Component.Builder
    interface Builder {
        fun timerModule(timerModule: TimerModule): Builder
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): TimerComponent
    }

    fun inject(timerFragment: TimerFragment)
    fun inject(timerTileService: TimerTileService)
}