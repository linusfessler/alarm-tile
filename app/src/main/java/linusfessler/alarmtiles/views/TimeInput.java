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

public class TimeInput extends LinearLayout {

    private static final String ILLEGAL_TIME_UNIT_ERROR_MESSAGE = "Add cases to handle new time units.";

    private TextInputLayout timeInputLayout;
    private TextInputEditText timeInputEditText;
    private Spinner timeUnitSpinner;

    private final BehaviorSubject<Long> millisSubject = BehaviorSubject.createDefault(0L);
    private final BehaviorSubject<TimeUnit> timeUnitSubject = BehaviorSubject.createDefault(TimeUnit.MINUTES);

    public TimeInput(@NonNull final Context context) {
        super(context);
        this.init(context, null);
    }

    public TimeInput(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public TimeInput(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.time_input, this);

        this.timeInputLayout = root.findViewById(R.id.time_input_layout);
        this.timeInputEditText = root.findViewById(R.id.time_input_edit_text);
        this.timeUnitSpinner = root.findViewById(R.id.time_unit_input);

        this.timeInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // Not needed for simple change detection
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                TimeInput.this.onTimeUpdated();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                // Not needed for simple change detection
            }
        });

        this.timeUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                TimeInput.this.onTimeUnitUpdated();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                // Not needed for spinner with static items
            }
        });

        if (attrs != null) {
            this.initWithAttrs(context, attrs);
        }
    }

    private void initWithAttrs(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TimeInput);

        final Drawable icon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon);
        if (icon != null) {
            this.setIcon(icon);
        }

        final String hint = styledAttributes.getString(R.styleable.TimeInput_hint);
        this.setHint(hint);

        styledAttributes.recycle();
    }

    private void onTimeUpdated() {
        if (this.timeInputEditText.getText() == null || this.timeInputEditText.getText().length() == 0) {
            this.millisSubject.onNext(0L);
            return;
        }

        final double time = Double.parseDouble(this.timeInputEditText.getText().toString());
        final TimeUnit timeUnit = this.timeUnitSubject.getValue();

        final double millis;
        switch (timeUnit) {
            case HOURS:
                millis = time * 60 * 60 * 1000;
                break;
            case MINUTES:
                millis = time * 60 * 1000;
                break;
            case SECONDS:
                millis = time * 1000;
                break;
            default:
                throw new IllegalStateException(TimeInput.ILLEGAL_TIME_UNIT_ERROR_MESSAGE);
        }

        this.millisSubject.onNext((long) millis);
    }

    private void onTimeUnitUpdated() {
        final int position = this.timeUnitSpinner.getSelectedItemPosition();

        final TimeUnit timeUnit;
        switch (position) {
            case 0:
                timeUnit = TimeUnit.HOURS;
                break;
            case 1:
                timeUnit = TimeUnit.MINUTES;
                break;
            case 2:
                timeUnit = TimeUnit.SECONDS;
                break;
            default:
                throw new IllegalStateException(TimeInput.ILLEGAL_TIME_UNIT_ERROR_MESSAGE);
        }

        this.timeUnitSubject.onNext(timeUnit);

        this.onTimeUpdated();
    }

    public long getMillis() {
        return this.millisSubject.getValue();
    }

    public Observable<Long> getMillisObservable() {
        return this.millisSubject;
    }

    public void setMillis(final long millis) {
        final TimeUnit timeUnit = this.timeUnitSubject.getValue();

        final long time;
        switch (timeUnit) {
            case HOURS:
                time = millis / (1000 * 60 * 60);
                break;
            case MINUTES:
                time = millis / (1000 * 60);
                break;
            case SECONDS:
                time = millis / 1000;
                break;
            default:
                throw new IllegalStateException(TimeInput.ILLEGAL_TIME_UNIT_ERROR_MESSAGE);
        }

        this.timeInputEditText.setText(String.valueOf(time));
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnitSubject.getValue();
    }

    public Observable<TimeUnit> getTimeUnitObservable() {
        return this.timeUnitSubject;
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

        this.timeUnitSpinner.setSelection(position);
    }

    public void setIcon(final Drawable icon) {
        this.timeInputLayout.setStartIconDrawable(icon);
    }

    public void setIcon(final int iconResourceId) {
        this.timeInputLayout.setStartIconDrawable(iconResourceId);
    }

    public void setHint(final String hint) {
        this.timeInputLayout.setHint(hint);
    }

    public void setHint(final int hintResourceId) {
        final String hint = this.getContext().getString(hintResourceId);
        this.timeInputLayout.setHint(hint);
    }

    public void showKeyboard(final InputMethodManager inputMethodManager) {
        if (this.timeInputEditText.requestFocus()) {
            this.timeInputEditText.post(() ->
                    inputMethodManager.showSoftInput(this.timeInputEditText, 0));
        }
    }
}
