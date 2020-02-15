package linusfessler.alarmtiles.sleeptimer

import java.util.concurrent.TimeUnit

interface SleepTimerEffect {
    class LoadFromDatabase : SleepTimerEffect

    data class SaveToDatabase(val sleepTimer: SleepTimer) : SleepTimerEffect

    data class StartWith(val time: Double, val timeUnit: TimeUnit) : SleepTimerEffect

    data class FinishWith(val sleepTimer: SleepTimer) : SleepTimerEffect

    data class ScheduleVolumeDecrease(val millisLeft: Long) : SleepTimerEffect

    class UnscheduleVolumeDecrease : SleepTimerEffect

    data class ScheduleFinish(val millisLeft: Long) : SleepTimerEffect

    class UnscheduleFinish : SleepTimerEffect

    class SetVolumeToZero : SleepTimerEffect

    class StopMediaPlayback : SleepTimerEffect

    class ShowNotification : SleepTimerEffect

    class HideNotification : SleepTimerEffect
}