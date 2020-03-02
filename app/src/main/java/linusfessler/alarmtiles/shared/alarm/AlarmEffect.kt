package linusfessler.alarmtiles.shared.alarm

import linusfessler.alarmtiles.shared.data.Time
import linusfessler.alarmtiles.shared.data.TimeOfDay

interface AlarmEffect {
    data class LoadFromDatabase(val unused: Byte = 0) : AlarmEffect

    data class SaveToDatabase(val alarm: Alarm) : AlarmEffect

    data class SetAtTimeOfDay(val timeOfDay: TimeOfDay) : AlarmEffect

    data class SetAfterDuration(val duration: Time) : AlarmEffect

    data class SetAlarm(val triggerTimestamp: Long) : AlarmEffect

    data class CancelAlarm(val unused: Byte = 0) : AlarmEffect
}