package linusfessler.alarmtiles.tiles.alarmtimer

import android.content.Context
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.alarm.AlarmEvent
import linusfessler.alarmtiles.shared.data.Time
import linusfessler.alarmtiles.shared.views.TimeInputDialog

class AlarmTimerStartDialog(
        context: Context,
        inputMethodManager: InputMethodManager,
        private val viewModel: AlarmTimerViewModel
) : TimeInputDialog(context, inputMethodManager) {
    private val disposable = CompositeDisposable()

    init {
        setTitle(R.string.alarm_timer)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_ok)) { _, _ ->
            timeInput.apply {
                viewModel.dispatch(AlarmEvent.SetAfterDuration(Time(time, timeUnit)))
            }
        }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel)) { _, _ -> }
    }

    override fun onStart() {
        super.onStart()

        disposable.add(viewModel.alarm
                .take(1)
                .subscribe {
                    timeInput.apply {
                        time = it.duration.time
                        timeUnit = it.duration.timeUnit
                    }
                })

        disposable.add(viewModel.alarm
                .subscribe {
                    // It's possible that the alarm timer was enabled through the quick settings while this dialog is shown, dismiss it in this case
                    if (it.isEnabled) {
                        dismiss()
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}