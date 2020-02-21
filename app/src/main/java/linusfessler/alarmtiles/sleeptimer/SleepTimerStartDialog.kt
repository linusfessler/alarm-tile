package linusfessler.alarmtiles.sleeptimer

import android.content.Context
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.Disposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.views.TimeInputDialog

class SleepTimerStartDialog(
        context: Context,
        inputMethodManager: InputMethodManager,
        private val viewModel: SleepTimerViewModel
) : TimeInputDialog(context, inputMethodManager) {
    private lateinit var disposable: Disposable

    override fun onStart() {
        super.onStart()
        disposable = viewModel.sleepTimer
                .subscribe {
                    timeInput.time = it.time
                    timeInput.timeUnit = it.timeUnit
                }
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    init {
        setTitle(R.string.sleep_timer)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_ok)) { _, _ ->
            val time = timeInput.time
            val timeUnit = timeInput.timeUnit
            viewModel.dispatch(SleepTimerEvent.Start(time, timeUnit))
        }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel)) { _, _ -> }
    }
}