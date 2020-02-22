package linusfessler.alarmtiles.stopwatch

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import javax.inject.Singleton

@Singleton
@Component(modules = [StopwatchModule::class])
interface StopwatchComponent {
    @Component.Builder
    interface Builder {
        fun stopwatchModule(stopwatchModule: StopwatchModule): Builder
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): StopwatchComponent
    }

    fun inject(stopwatchFragment: StopwatchFragment)
    fun inject(stopwatchTileService: StopwatchTileService)
}