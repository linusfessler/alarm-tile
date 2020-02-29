package linusfessler.alarmtiles.tiles.alarm

interface AlarmEvent {
    data class Resume(val unused: Byte = 0) : AlarmEvent

    data class Resumed(val alarm: Alarm) : AlarmEvent

    data class Enable(val hourOfDay: Int, val minuteOfHour: Int) : AlarmEvent

    data class EnableWith(val triggerTimestamp: Long, val hourOfDay: Int, val minuteOfHour: Int) : AlarmEvent

    data class Disable(val unused: Byte = 0) : AlarmEvent
}