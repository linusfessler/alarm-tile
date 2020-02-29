package linusfessler.alarmtiles.tiles.sleeptimer

import linusfessler.alarmtiles.shared.data.Time

interface SleepTimerEffect {
    data class LoadFromDatabase(val unused: Byte = 0) : SleepTimerEffect

    data class SaveToDatabase(val sleepTimer: SleepTimer) : SleepTimerEffect

    data class StartWith(val duration: Time) : SleepTimerEffect

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