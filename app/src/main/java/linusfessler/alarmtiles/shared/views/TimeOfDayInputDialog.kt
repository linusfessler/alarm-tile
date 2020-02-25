package linusfessler.alarmtiles.shared.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import linusfessler.alarmtiles.R

open class TimeOfDayInputDialog constructor(context: Context) : AlertDialog(context) {
    val timePicker: TimePicker

    init {
        @SuppressLint("InflateParams")
        timePicker = layoutInflater.inflate(R.layout.time_picker, null) as TimePicker
        this.setView(timePicker)
    }
}