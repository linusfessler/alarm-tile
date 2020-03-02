package linusfessler.alarmtiles.tiles.alarm

import android.content.Context
import android.content.DialogInterface
import android.widget.TimePicker
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.alarm.AlarmEvent
import linusfessler.alarmtiles.shared.data.TimeOfDay
import linusfessler.alarmtiles.shared.views.TimeOfDayInputDialog

class AlarmStartDialog constructor(context: Context, private val viewModel: AlarmViewModel, is24Hours: Boolean) : TimeOfDayInputDialog(context, is24Hours) {
    private val disposable = CompositeDisposable()

    private var hourOfDay = 0
    private var minuteOfHour = 0

    init {
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_ok)) { _: DialogInterface, _: Int ->
            viewModel.dispatch(AlarmEvent.SetAtTimeOfDay(TimeOfDay(hourOfDay, minuteOfHour)))
        }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel)) { _: DialogInterface, _: Int -> }
    }

    override fun onStart() {
        super.onStart()

        disposable.add(viewModel.alarm
                .take(1)
                .subscribe {
                    timePicker.apply {
                        hour = it.timeOfDay.hourOfDay
                        minute = it.timeOfDay.minuteOfHour
                    }
                })

        disposable.add(viewModel.alarm
                .subscribe {
                    // It's possible that the alarm was enabled through the quick settings while this dialog is shown, dismiss it in this case
                    if (it.isEnabled) {
                        dismiss()
                    }
                })

        timePicker.setOnTimeChangedListener { _: TimePicker, hourOfDay: Int, minuteOfHour: Int ->
            this.hourOfDay = hourOfDay
            this.minuteOfHour = minuteOfHour
        }
    }

    override fun onStop() {
        super.onStop()
        timePicker.setOnTimeChangedListener(null)
        disposable.clear()
    }
}