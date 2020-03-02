package linusfessler.alarmtiles.shared

import dagger.Component
import linusfessler.alarmtiles.shared.alarm.config.AlarmConfigFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [SharedModule::class])
interface SharedComponent {
    @Component.Builder
    interface Builder {
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): SharedComponent
    }

    fun inject(app: App)
    fun inject(alarmConfigFragment: AlarmConfigFragment)
    fun inject(alarmService: AlarmService)
}