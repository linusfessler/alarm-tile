package linusfessler.alarmtiles.alarm

interface AlarmEffect {
    class LoadFromDatabase : AlarmEffect

    data class SaveToDatabase(val alarm: Alarm) : AlarmEffect

    class Enable(val hourOfDay: Int, val minuteOfHour: Int) : AlarmEffect

    class Disable : AlarmEffect

    data class SetAlarm(val triggerTimestamp: Long) : AlarmEffect

    class CancelAlarm : AlarmEffect
}