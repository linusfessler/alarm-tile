package linusfessler.alarmtiles.tiles.alarm

interface AlarmEffect {
    data class LoadFromDatabase(val unused: Byte = 0) : AlarmEffect

    data class SaveToDatabase(val alarm: Alarm) : AlarmEffect

    data class Enable(val hourOfDay: Int, val minuteOfHour: Int) : AlarmEffect

    data class Disable(val unused: Byte = 0) : AlarmEffect

    data class SetAlarm(val triggerTimestamp: Long) : AlarmEffect

    data class CancelAlarm(val unused: Byte = 0) : AlarmEffect
}