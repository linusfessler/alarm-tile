package linusfessler.alarmtiles.shared

import android.app.Application
import linusfessler.alarmtiles.tiles.alarm.AlarmComponent
import linusfessler.alarmtiles.tiles.alarm.AlarmModule
import linusfessler.alarmtiles.tiles.alarm.DaggerAlarmComponent
import linusfessler.alarmtiles.tiles.alarmtimer.AlarmTimerComponent
import linusfessler.alarmtiles.tiles.alarmtimer.AlarmTimerModule
import linusfessler.alarmtiles.tiles.alarmtimer.DaggerAlarmTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.DaggerSleepTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerModule
import linusfessler.alarmtiles.tiles.stopwatch.DaggerStopwatchComponent
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchComponent
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchModule

class App : Application() {
    private val sharedModule: SharedModule = SharedModule(this)

    val sharedComponent: SharedComponent = DaggerSharedComponent.builder()
            .sharedModule(sharedModule)
            .build()

    val alarmComponent: AlarmComponent = DaggerAlarmComponent.builder()
            .alarmModule(AlarmModule())
            .sharedModule(sharedModule)
            .build()

    val alarmTimerComponent: AlarmTimerComponent = DaggerAlarmTimerComponent.builder()
            .alarmTimerModule(AlarmTimerModule())
            .sharedModule(sharedModule)
            .build()

    val sleepTimerComponent: SleepTimerComponent = DaggerSleepTimerComponent.builder()
            .sleepTimerModule(SleepTimerModule())
            .sharedModule(sharedModule)
            .build()

    val stopwatchComponent: StopwatchComponent = DaggerStopwatchComponent.builder()
            .stopwatchModule(StopwatchModule())
            .sharedModule(sharedModule)
            .build()

//    @Inject
//    lateinit var uiModeManager: UiModeManager

    override fun onCreate() {
        super.onCreate()
        sharedComponent.inject(this)
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            uiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
//        }

//        @Inject
//        lateinit var uiModeManager: UiModeManager
//        uiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}