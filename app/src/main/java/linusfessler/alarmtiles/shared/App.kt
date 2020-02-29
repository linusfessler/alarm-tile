package linusfessler.alarmtiles.shared

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import linusfessler.alarmtiles.tiles.alarm.AlarmComponent
import linusfessler.alarmtiles.tiles.alarm.AlarmModule
import linusfessler.alarmtiles.tiles.alarm.DaggerAlarmComponent
import linusfessler.alarmtiles.tiles.sleeptimer.DaggerSleepTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerModule
import linusfessler.alarmtiles.tiles.stopwatch.DaggerStopwatchComponent
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchComponent
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchModule
import linusfessler.alarmtiles.tiles.timer.DaggerTimerComponent
import linusfessler.alarmtiles.tiles.timer.TimerComponent
import linusfessler.timertiles.tiles.timer.TimerModule

class App : Application() {
    private val sharedModule: SharedModule = SharedModule(this)

    val alarmComponent: AlarmComponent = DaggerAlarmComponent.builder()
            .alarmModule(AlarmModule())
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

    val timerComponent: TimerComponent = DaggerTimerComponent.builder()
            .timerModule(TimerModule())
            .sharedModule(sharedModule)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}