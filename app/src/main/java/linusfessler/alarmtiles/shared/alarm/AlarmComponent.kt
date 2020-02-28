package linusfessler.alarmtiles.shared.alarm

import dagger.Component
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.tiles.alarm.AlarmTileFragment
import linusfessler.alarmtiles.tiles.alarm.AlarmTileService
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

    fun inject(alarmTileFragment: AlarmTileFragment)
    fun inject(alarmTileService: AlarmTileService)
}