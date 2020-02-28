package linusfessler.alarmtiles.shared.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import linusfessler.alarmtiles.R

open class TimeOfDayInputDialog constructor(context: Context, is24Hours: Boolean) : AlertDialog(context) {
    val timePicker: TimePicker

    init {
        @SuppressLint("InflateParams")
        val view = layoutInflater.inflate(R.layout.dialog_time_of_day_input, null)
        this.setView(view)
        
        timePicker = view.findViewById(R.id.time_of_day_input)
        timePicker.setIs24HourView(is24Hours)
    }
}