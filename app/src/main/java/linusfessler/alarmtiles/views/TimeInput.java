package linusfessler.alarmtiles.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import linusfessler.alarmtiles.R;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TimeInput extends LinearLayout {

    private static final String ILLEGAL_TIME_UNIT_ERROR_MESSAGE = "Add cases to handle new time units.";

    private TextInputLayout timeInputLayout;
    private TextInputEditText timeInputEditText;
    private Spinner timeUnitSpinner;

    private final BehaviorSubject<Double> timeSubject = BehaviorSubject.createDefault(0d);
    private final BehaviorSubject<TimeUnit> timeUnitSubject = BehaviorSubject.createDefault(MINUTES);

    public TimeInput(@NonNull final Context context) {
        super(context);
        init(context, null);
    }

    public TimeInput(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TimeInput(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.time_input, this);

        timeInputLayout = root.findViewById(R.id.time_input_layout);
        timeInputEditText = root.findViewById(R.id.time_input_edit_text);
        timeUnitSpinner = root.findViewById(R.id.time_unit_input);

        timeInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // Not needed for simple change detection
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if (timeInputEditText.getText() == null || timeInputEditText.getText().length() == 0) {
                    timeSubject.onNext(0d);
                    return;
                }

                final double time = Double.parseDouble(timeInputEditText.getText().toString());
                if (time != getTime()) {
                    timeSubject.onNext(time);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                // Not needed for simple change detection
            }
        });

        timeUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                final TimeUnit timeUnit;
                switch (position) {
                    case 0:
                        timeUnit = HOURS;
                        break;
                    case 1:
                        timeUnit = MINUTES;
                        break;
                    case 2:
                        timeUnit = SECONDS;
                        break;
                    default:
                        throw new IllegalStateException(TimeInput.ILLEGAL_TIME_UNIT_ERROR_MESSAGE);
                }

                if (timeUnit != getTimeUnit()) {
                    timeUnitSubject.onNext(timeUnit);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                // Not needed for spinner with static items
            }
        });

        if (attrs != null) {
            initWithAttrs(context, attrs);
        }
    }

    private void initWithAttrs(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TimeInput);

        final Drawable icon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon);
        if (icon != null) {
            setIcon(icon);
        }

        final String hint = styledAttributes.getString(R.styleable.TimeInput_hint);
        setHint(hint);

        styledAttributes.recycle();
    }

    public double getTime() {
        return timeSubject.getValue();
    }

    public Observable<Double> getTimeObservable() {
        return timeSubject.hide();
    }

    public void setTime(final double time) {
        final String timeString;
        if (time % 1 == 0) {
            timeString = String.valueOf((long) time);
        } else {
            timeString = String.valueOf(time);
        }
        timeInputEditText.setText(timeString);
    }

    public TimeUnit getTimeUnit() {
        return timeUnitSubject.getValue();
    }

    public Observable<TimeUnit> getTimeUnitObservable() {
        return timeUnitSubject.hide();
    }

    public void setTimeUnit(final TimeUnit timeUnit) {
        final int position;
        switch (timeUnit) {
            case HOURS:
                position = 0;
                break;
            case MINUTES:
                position = 1;
                break;
            case SECONDS:
                position = 2;
                break;
            default:
                throw new IllegalStateException(TimeInput.ILLEGAL_TIME_UNIT_ERROR_MESSAGE);
        }

        timeUnitSpinner.setSelection(position);
    }

    public void setIcon(final Drawable icon) {
        timeInputLayout.setStartIconDrawable(icon);
    }

    public void setIcon(final int iconResourceId) {
        timeInputLayout.setStartIconDrawable(iconResourceId);
    }

    public void setHint(final String hint) {
        timeInputLayout.setHint(hint);
    }

    public void setHint(final int hintResourceId) {
        final String hint = getContext().getString(hintResourceId);
        timeInputLayout.setHint(hint);
    }

    public void showKeyboard(final InputMethodManager inputMethodManager) {
        if (timeInputEditText.requestFocus()) {
            timeInputEditText.post(() ->
                    inputMethodManager.showSoftInput(timeInputEditText, 0));
        }
    }
}
