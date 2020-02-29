package linusfessler.alarmtiles.tiles.alarmtimer

import linusfessler.alarmtiles.shared.data.Time

interface AlarmTimerEffect {
    data class LoadFromDatabase(val unused: Byte = 0) : AlarmTimerEffect

    data class SaveToDatabase(val alarmTimer: AlarmTimer) : AlarmTimerEffect

    data class Enable(val duration: Time) : AlarmTimerEffect

    data class Disable(val unused: Byte = 0) : AlarmTimerEffect

    data class SetAlarm(val triggerTimestamp: Long) : AlarmTimerEffect

    data class CancelAlarm(val unused: Byte = 0) : AlarmTimerEffect
}