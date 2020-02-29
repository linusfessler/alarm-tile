package linusfessler.alarmtiles.tiles.sleeptimer

import linusfessler.alarmtiles.shared.data.Time

interface SleepTimerEvent {
    data class VolumeChanged(val volume: Int) : SleepTimerEvent

    class Initialize : SleepTimerEvent

    data class Initialized(val sleepTimer: SleepTimer) : SleepTimerEvent

    data class Start(val duration: Time) : SleepTimerEvent

    data class StartWith(val startTimestamp: Long, val duration: Time) : SleepTimerEvent

    data class SetDecreasingVolume(val decreasingVolume: Boolean) : SleepTimerEvent

    class Cancel : SleepTimerEvent

    class Finish : SleepTimerEvent

    class FinishWith(val sleepTimer: SleepTimer) : SleepTimerEvent
}