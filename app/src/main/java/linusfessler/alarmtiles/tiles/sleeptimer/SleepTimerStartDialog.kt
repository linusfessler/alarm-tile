package linusfessler.alarmtiles.tiles.sleeptimer

import android.content.Context
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.shared.views.TimeInputDialog

class SleepTimerStartDialog(
        context: Context,
        inputMethodManager: InputMethodManager,
        private val viewModel: SleepTimerViewModel
) : TimeInputDialog(context, inputMethodManager) {
    private val disposable = CompositeDisposable()

    init {
        setTitle(R.string.sleep_timer)
        setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_ok)) { _, _ ->
            timeInput.apply {
                viewModel.dispatch(SleepTimerEvent.Start(time, timeUnit))
            }
        }
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.dialog_cancel)) { _, _ -> }
    }

    override fun onStart() {
        super.onStart()

        disposable.add(viewModel.sleepTimer
                .take(1)
                .subscribe {
                    timeInput.apply {
                        time = it.time
                        timeUnit = it.timeUnit
                    }
                })

        disposable.add(viewModel.sleepTimer
                .subscribe {
                    // It's possible that the sleep timer was enabled through the quick settings while this dialog is shown, dismiss it in this case
                    if (it.isEnabled) {
                        dismiss()
                    }
                })
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }
}