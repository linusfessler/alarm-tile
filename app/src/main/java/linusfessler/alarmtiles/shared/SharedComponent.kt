package linusfessler.alarmtiles.shared

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SharedModule::class])
interface SharedComponent {
    @Component.Builder
    interface Builder {
        fun sharedModule(sharedModule: SharedModule): Builder
        fun build(): SharedComponent
    }
}