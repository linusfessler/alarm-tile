package linusfessler.alarmtiles.tiles.alarm

import linusfessler.alarmtiles.shared.data.TimeOfDay

interface AlarmEvent {
    data class Resume(val unused: Byte = 0) : AlarmEvent

    data class Resumed(val alarm: Alarm) : AlarmEvent

    data class Enable(val timeOfDay: TimeOfDay) : AlarmEvent

    data class EnableWith(val timeOfDay: TimeOfDay, val triggerTimestamp: Long) : AlarmEvent

    data class Disable(val unused: Byte = 0) : AlarmEvent
}