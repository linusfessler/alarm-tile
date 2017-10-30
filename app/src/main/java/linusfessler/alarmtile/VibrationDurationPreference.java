package linusfessler.alarmtile;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class VibrationDurationPreference extends EditTextPreference {

    private long millis = 0;

    public VibrationDurationPreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (defaultValue != null) {
            millis = Long.parseLong(getPersistedString(String.valueOf(defaultValue)));
        } else {
            millis = Long.parseLong(getPersistedString("500"));
        }
        setSummary(getSummary());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String newMillis = getEditText().getText().toString();
            millis = Long.parseLong(newMillis);
            setSummary(getSummary());
            if (callChangeListener(newMillis)) {
                persistString(newMillis);
            }
        }
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.formatMillis(millis);
    }
}