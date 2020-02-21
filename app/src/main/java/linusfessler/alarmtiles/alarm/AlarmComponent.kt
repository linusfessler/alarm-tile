package linusfessler.alarmtiles.alarm

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AlarmModule::class])
interface AlarmComponent {
    @Component.Builder
    interface Builder {
        fun alarmModule(alarmModule: AlarmModule): Builder
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): AlarmComponent
    }

    fun inject(alarmFragment: AlarmFragment)
    fun inject(alarmTileService: AlarmTileService)
}