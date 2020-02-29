package linusfessler.alarmtiles.shared.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import linusfessler.alarmtiles.R
import java.util.concurrent.TimeUnit

class TimeInput : LinearLayout {
    private lateinit var timeInputLayout: TextInputLayout
    private lateinit var timeInputEditText: TextInputEditText
    private lateinit var timeUnitSpinner: Spinner

    private val timeSubject = BehaviorSubject.createDefault(0.0)
    private val timeUnitSubject = BehaviorSubject.createDefault(TimeUnit.MINUTES)

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = LayoutInflater.from(context)
        val root = inflater.inflate(R.layout.time_input, this)
        timeInputLayout = root.findViewById(R.id.time_input_layout)
        timeInputEditText = root.findViewById(R.id.time_input_edit_text)
        timeUnitSpinner = root.findViewById(R.id.time_unit_input)

        timeInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not needed for simple change detection
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (timeInputEditText.text.isNullOrEmpty()) {
                    timeSubject.onNext(0.0)
                    return
                }

                val text = timeInputEditText.text.toString()

                if (text == ".") {
                    return
                }

                val time = timeInputEditText.text.toString().toDouble()
                if (time != this@TimeInput.time) {
                    timeSubject.onNext(time)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Not needed for simple change detection
            }
        })

        timeUnitSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val timeUnit: TimeUnit = when (position) {
                    0 -> TimeUnit.HOURS
                    1 -> TimeUnit.MINUTES
                    2 -> TimeUnit.SECONDS
                    else -> throw IllegalStateException(ILLEGAL_TIME_UNIT_ERROR_MESSAGE)
                }

                if (timeUnit != this@TimeInput.timeUnit) {
                    timeUnitSubject.onNext(timeUnit)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Not needed for spinner with static items
            }
        }
        attrs?.let {
            initWithAttrs(context, it)
        }
    }

    private fun initWithAttrs(context: Context, attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TimeInput)
        val icon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon)
        val hint = styledAttributes.getString(R.styleable.TimeInput_hint)
        styledAttributes.recycle()

        icon?.let {
            setIcon(it)
        }

        hint?.let {
            setHint(it)
        }
    }

    var time: Double
        get() = timeSubject.value!!
        set(time) {
            val timeString: String = if (time % 1 == 0.0) {
                // Remove unnecessary ".0"
                time.toLong().toString()
            } else {
                time.toString()
            }

            timeInputEditText.setText(timeString)
            timeInputEditText.setSelection(timeString.length)
        }

    val timeObservable: Observable<Double> = timeSubject.hide()

    var timeUnit: TimeUnit
        get() = timeUnitSubject.value!!
        set(timeUnit) {
            val position: Int = when (timeUnit) {
                TimeUnit.HOURS -> 0
                TimeUnit.MINUTES -> 1
                TimeUnit.SECONDS -> 2
                else -> throw IllegalStateException(ILLEGAL_TIME_UNIT_ERROR_MESSAGE)
            }

            timeUnitSpinner.setSelection(position)
        }

    val timeUnitObservable: Observable<TimeUnit> = timeUnitSubject.hide()

    fun setIcon(icon: Drawable) {
        timeInputLayout.startIconDrawable = icon
    }

    fun setIcon(iconResourceId: Int) {
        timeInputLayout.setStartIconDrawable(iconResourceId)
    }

    fun setHint(hint: String) {
        timeInputLayout.hint = hint
    }

    fun setHint(hintResourceId: Int) {
        val hint = context.getString(hintResourceId)
        timeInputLayout.hint = hint
    }

    fun showKeyboard(inputMethodManager: InputMethodManager) {
        timeInputEditText.requestFocus().let {
            timeInputEditText.post { inputMethodManager.showSoftInput(timeInputEditText, 0) }
        }
    }

    companion object {
        private const val ILLEGAL_TIME_UNIT_ERROR_MESSAGE = "Add cases to handle new time units."
    }
}