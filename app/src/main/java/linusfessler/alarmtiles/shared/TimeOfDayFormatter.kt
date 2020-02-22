package linusfessler.alarmtiles.shared

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TimeOfDayFormatter @Inject constructor(@Named("is24Hours") is24Hours: Boolean) {
    private val format = if (is24Hours) {
        SimpleDateFormat("H:mm", Locale.UK)
    } else {
        SimpleDateFormat("h:mm a", Locale.US)
    }

    fun format(hourOfDay: Int, minutesOfHour: Int): String {
        val calendar = GregorianCalendar(0, 0, 0, hourOfDay, minutesOfHour)
        val timeOfDay = calendar.time
        return format.format(timeOfDay)
    }
}