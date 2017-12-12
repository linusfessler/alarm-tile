package linusfessler.alarmtile.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import linusfessler.alarmtile.R;
import linusfessler.alarmtile.utility.TimeFormatter;

public class TimePreference extends DialogPreference {

    private TimePicker timePicker;
    private int hours = 0;
    private int minutes = 0;

    public TimePreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected View onCreateDialogView() {
        timePicker = (TimePicker) LayoutInflater.from(getContext()).inflate(R.layout.time_dialog, null);
        timePicker.setIs24HourView(true);
        return timePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        timePicker.setHour(hours);
        timePicker.setMinute(minutes);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        int timeInMillis;
        if (restoreValue) {
            timeInMillis = getPersistedInt(0);
        } else {
            timeInMillis = (Integer) defaultValue;
        }
        persistInt(timeInMillis);
        int timeInMinutes = timeInMillis / 60000;
        hours = timeInMinutes / 60;
        minutes = timeInMinutes % 60;
        setSummary(getSummary());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            hours = timePicker.getHour();
            minutes = timePicker.getMinute();
            int timeInMinutes = 60 * hours + minutes;
            int timeInMillis = 60000 * timeInMinutes;
            if (callChangeListener(timeInMillis)) {
                persistInt(timeInMillis);
                setSummary(getSummary());
            }
        }
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.formatHoursMinutes(hours, minutes);
    }
}