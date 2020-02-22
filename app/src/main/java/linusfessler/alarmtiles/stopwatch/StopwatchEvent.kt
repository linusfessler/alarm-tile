package linusfessler.alarmtiles.stopwatch

interface StopwatchEvent {
    class Initialize : StopwatchEvent

    data class Initialized(val stopwatch: Stopwatch) : StopwatchEvent

    class Toggle : StopwatchEvent

    data class Start(val startTimestamp: Long) : StopwatchEvent

    data class Stop(val stopTimestamp: Long) : StopwatchEvent
}