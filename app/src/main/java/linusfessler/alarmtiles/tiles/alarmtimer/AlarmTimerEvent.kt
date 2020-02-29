package linusfessler.alarmtiles.tiles.alarmtimer

import linusfessler.alarmtiles.shared.data.Time

interface AlarmTimerEvent {
    data class Resume(val unused: Byte = 0) : AlarmTimerEvent

    data class Resumed(val alarmTimer: AlarmTimer) : AlarmTimerEvent

    data class Enable(val duration: Time) : AlarmTimerEvent

    data class EnableWith(val duration: Time, val triggerTimestamp: Long) : AlarmTimerEvent

    data class Disable(val unused: Byte = 0) : AlarmTimerEvent
}