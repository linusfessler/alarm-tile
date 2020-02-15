package linusfessler.alarmtiles.sleeptimer

import java.util.concurrent.TimeUnit

interface SleepTimerEvent {
    data class VolumeChanged(val volume: Int) : SleepTimerEvent

    class Load : SleepTimerEvent

    data class Loaded(val sleepTimer: SleepTimer) : SleepTimerEvent

    data class Start(val time: Double, val timeUnit: TimeUnit) : SleepTimerEvent

    data class StartWith(val startTimestamp: Long, val time: Double, val timeUnit: TimeUnit) : SleepTimerEvent

    data class SetDecreasingVolume(val decreasingVolume: Boolean) : SleepTimerEvent

    class Cancel : SleepTimerEvent

    class Finish : SleepTimerEvent

    class FinishWith(val sleepTimer: SleepTimer) : SleepTimerEvent
}