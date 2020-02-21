package linusfessler.alarmtiles.stopwatch

interface StopwatchEffect {
    class LoadFromDatabase : StopwatchEffect

    data class SaveToDatabase(val stopwatch: Stopwatch) : StopwatchEffect

    class Start : StopwatchEffect

    class Stop : StopwatchEffect
}