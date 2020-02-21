package linusfessler.alarmtiles.shared

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeOfDayFormatter @Inject constructor() {
    fun format(hourOfDay: Int, minutesOfHour: Int, is24Hours: Boolean): String {
        val calendar: Calendar = GregorianCalendar(0, 0, 0, hourOfDay, minutesOfHour)
        val timeOfDay = calendar.time
        val displayFormat = getFormat(is24Hours)
        return displayFormat.format(timeOfDay)
    }

    private fun getFormat(is24Hours: Boolean): SimpleDateFormat {
        return if (is24Hours) {
            SimpleDateFormat("H:mm", Locale.UK)
        } else {
            SimpleDateFormat("h:mm a", Locale.US)
        }
    }
}