package linusfessler.alarmtiles.shared.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R

open class TimeInputDialog constructor(context: Context) : AlertDialog(context) {
    val timeInput: TimeInput

    private val disposable = CompositeDisposable()

    constructor(context: Context, inputMethodManager: InputMethodManager) : this(context) {
        setOnShowListener {
            timeInput.showKeyboard(inputMethodManager)
        }
    }

    init {
        @SuppressLint("InflateParams")
        val view = layoutInflater.inflate(R.layout.dialog_time_input, null)
        this.setView(view)
        timeInput = view.findViewById(R.id.time_input)
    }

    override fun onStart() {
        super.onStart()
        disposable.add(timeInput.timeObservable
                .subscribe {
                    getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = it > 0
                })
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}