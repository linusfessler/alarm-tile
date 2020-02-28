package linusfessler.alarmtiles.core

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import linusfessler.alarmtiles.shared.DaggerSharedComponent
import linusfessler.alarmtiles.shared.SharedComponent
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.shared.alarm.AlarmComponent
import linusfessler.alarmtiles.shared.alarm.AlarmModule
import linusfessler.alarmtiles.shared.alarm.DaggerAlarmComponent
import linusfessler.alarmtiles.tiles.sleeptimer.DaggerSleepTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerComponent
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerModule
import linusfessler.alarmtiles.tiles.stopwatch.DaggerStopwatchComponent
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchComponent
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchModule
import linusfessler.alarmtiles.tiles.timer.DaggerTimerComponent
import linusfessler.alarmtiles.tiles.timer.TimerComponent
import linusfessler.alarmtiles.tiles.timer.TimerModule

class App : Application() {
    private val sharedModule: SharedModule = SharedModule(this)
    private val sharedComponent: SharedComponent = DaggerSharedComponent.builder()
            .sharedModule(sharedModule)
            .build()

    val sleepTimerComponent: SleepTimerComponent = DaggerSleepTimerComponent.builder()
            .sleepTimerModule(SleepTimerModule())
            .sharedModule(sharedModule)
            .build()

    val alarmComponent: AlarmComponent = DaggerAlarmComponent.builder()
            .alarmModule(AlarmModule())
            .sharedModule(sharedModule)
            .build()

    val timerComponent: TimerComponent = DaggerTimerComponent.builder()
            .timerModule(TimerModule())
            .sharedModule(sharedModule)
            .build()

    val stopwatchComponent: StopwatchComponent = DaggerStopwatchComponent.builder()
            .stopwatchModule(StopwatchModule())
            .sharedModule(sharedModule)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}