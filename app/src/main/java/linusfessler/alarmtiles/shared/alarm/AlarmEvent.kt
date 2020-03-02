package linusfessler.alarmtiles.shared.alarm

import linusfessler.alarmtiles.shared.data.Time
import linusfessler.alarmtiles.shared.data.TimeOfDay

interface AlarmEvent {
    data class Resume(val unused: Byte = 0) : AlarmEvent

    data class Resumed(val alarm: Alarm) : AlarmEvent

    data class SetAtTimeOfDay(val timeOfDay: TimeOfDay) : AlarmEvent

    data class SetAtTimeOfDayWithTimestamp(val timeOfDay: TimeOfDay, val triggerTimestamp: Long) : AlarmEvent

    data class SetAfterDuration(val duration: Time) : AlarmEvent

    data class SetAfterDurationWithTimestamp(val duration: Time, val triggerTimestamp: Long) : AlarmEvent

    data class Cancel(val unused: Byte = 0) : AlarmEvent
}