package linusfessler.alarmtiles.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import linusfessler.alarmtiles.R;

public class TimeInput extends LinearLayout {

    private TextInputEditText timeView;
    private Spinner timeUnitView;

    private final BehaviorSubject<Long> millisSubject = BehaviorSubject.create();

    public TimeInput(@NonNull final Context context) {
        super(context);
        this.init(context);
    }

    public TimeInput(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public TimeInput(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(@NonNull final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.time_input, this);

        this.timeView = root.findViewById(R.id.time);
        this.timeUnitView = root.findViewById(R.id.time_unit);

        this.timeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // Not needed for simple change detection
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                TimeInput.this.update();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                // Not needed for simple change detection
            }
        });

        this.timeUnitView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                TimeInput.this.update();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                // Not needed for spinner with static items
            }
        });
    }

    private void update() {
        if (this.timeView.getText() == null || this.timeView.getText().length() == 0) {
            this.millisSubject.onNext(0L);
            return;
        }

        final double time = Double.parseDouble(this.timeView.getText().toString());
        final int position = this.timeUnitView.getSelectedItemPosition();

        final double millis;
        switch (position) {
            case 0:
                millis = time * 60 * 60 * 1000;
                break;
            case 1:
                millis = time * 60 * 1000;
                break;
            case 2:
                millis = time * 1000;
                break;
            default:
                throw new IllegalStateException("Add cases to handle new time units.");
        }

        this.millisSubject.onNext((long) millis);
    }

    public Observable<Long> getMillis() {
        return this.millisSubject;
    }
}
