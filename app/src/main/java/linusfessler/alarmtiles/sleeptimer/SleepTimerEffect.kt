package linusfessler.alarmtiles.sleeptimer

interface SleepTimerEffect {
    class LoadFromDatabase : SleepTimerEffect

    data class SaveToDatabase(val sleepTimer: SleepTimer) : SleepTimerEffect

    class Start : SleepTimerEffect

    class Cancel : SleepTimerEffect

    data class StartWith(val sleepTimer: SleepTimer) : SleepTimerEffect

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