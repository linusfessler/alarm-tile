package linusfessler.alarmtiles.tiles.stopwatch

interface StopwatchEffect {
    data class LoadFromDatabase(val unused: Byte = 0) : StopwatchEffect

    data class SaveToDatabase(val stopwatch: Stopwatch) : StopwatchEffect

    data class Start(val unused: Byte = 0) : StopwatchEffect

    data class Stop(val unused: Byte = 0) : StopwatchEffect
}