package linusfessler.alarmtiles.sleeptimer

import java.util.concurrent.TimeUnit

interface SleepTimerEvent {
    class Load : SleepTimerEvent

    data class Loaded(val sleepTimer: SleepTimer) : SleepTimerEvent

    class Toggle : SleepTimerEvent

    class Start : SleepTimerEvent

    data class StartWith(val sleepTimer: SleepTimer) : SleepTimerEvent

    data class VolumeChanged(val volume: Int) : SleepTimerEvent

    data class SetTime(val time: Double) : SleepTimerEvent

    class SetTimeUnit(val timeUnit: TimeUnit) : SleepTimerEvent

    data class SetDecreasingVolume(val decreasingVolume: Boolean) : SleepTimerEvent

    class Cancel : SleepTimerEvent

    class Finish : SleepTimerEvent

    class FinishWith(val sleepTimer: SleepTimer) : SleepTimerEvent
}