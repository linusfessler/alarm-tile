package linusfessler.alarmtiles.shared.alarm

interface AlarmEvent {
    class Initialize : AlarmEvent

    data class Initialized(val alarm: Alarm) : AlarmEvent

    data class Enable(val hourOfDay: Int, val minuteOfHour: Int) : AlarmEvent

    data class EnableWith(val hourOfDay: Int, val minuteOfHour: Int, val triggerTimestamp: Long) : AlarmEvent

    class Disable : AlarmEvent
}