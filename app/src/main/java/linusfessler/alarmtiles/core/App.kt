package linusfessler.alarmtiles.core

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import linusfessler.alarmtiles.alarm.AlarmComponent
import linusfessler.alarmtiles.alarm.AlarmModule
import linusfessler.alarmtiles.alarm.DaggerAlarmComponent
import linusfessler.alarmtiles.shared.DaggerSharedComponent
import linusfessler.alarmtiles.shared.SharedComponent
import linusfessler.alarmtiles.shared.SharedModule
import linusfessler.alarmtiles.sleeptimer.DaggerSleepTimerComponent
import linusfessler.alarmtiles.sleeptimer.SleepTimerComponent
import linusfessler.alarmtiles.sleeptimer.SleepTimerModule
import linusfessler.alarmtiles.stopwatch.DaggerStopwatchComponent
import linusfessler.alarmtiles.stopwatch.StopwatchComponent
import linusfessler.alarmtiles.stopwatch.StopwatchModule
import linusfessler.alarmtiles.timer.DaggerTimerComponent
import linusfessler.alarmtiles.timer.TimerComponent
import linusfessler.alarmtiles.timer.TimerModule

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