package linusfessler.alarmtiles.tiles.sleeptimer

import java.util.concurrent.TimeUnit

interface SleepTimerEffect {
    data class LoadFromDatabase(val unused: Byte = 0) : SleepTimerEffect

    data class SaveToDatabase(val sleepTimer: SleepTimer) : SleepTimerEffect

    data class StartWith(val time: Double, val timeUnit: TimeUnit) : SleepTimerEffect

    data class FinishWith(val sleepTimer: SleepTimer) : SleepTimerEffect

    data class ScheduleVolumeDecrease(val millisLeft: Long) : SleepTimerEffect

    data class UnscheduleVolumeDecrease(val unused: Byte = 0) : SleepTimerEffect

    data class ScheduleFinish(val millisLeft: Long) : SleepTimerEffect

    data class UnscheduleFinish(val unused: Byte = 0) : SleepTimerEffect

    data class SetVolumeToZero(val unused: Byte = 0) : SleepTimerEffect

    data class StopMediaPlayback(val unused: Byte = 0) : SleepTimerEffect

    data class ShowNotification(val unused: Byte = 0) : SleepTimerEffect

    data class HideNotification(val unused: Byte = 0) : SleepTimerEffect
}