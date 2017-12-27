package linusfessler.alarmtiles.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.utility.TimeFormatter;

public class TimePreference extends DialogPreference {

    protected TimePicker timePicker;
    protected int t1 = 0;
    protected int t2 = 0;

    public TimePreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected View onCreateDialogView() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useAnalogClocks = preferences.getBoolean(getContext().getString(R.string.pref_use_analog_clocks_key), false);
        int layoutId;
        if (useAnalogClocks) {
            layoutId = R.layout.time_picker_analog;
        } else {
            layoutId = R.layout.time_picker_digital;
        }
        timePicker = (TimePicker) LayoutInflater.from(getContext()).inflate(layoutId, null);
        return timePicker;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean useAnalogClocks = preferences.getBoolean(getContext().getString(R.string.pref_use_analog_clocks_key), false);
        CharSequence title;
        if (useAnalogClocks) {
            title = "";
        } else {
            title = getTitle();
        }
        builder.setTitle(title);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean use24hFormat = preferences.getBoolean(getContext().getString(R.string.pref_use_24h_format_key), false);
        timePicker.setIs24HourView(use24hFormat);
        timePicker.setHour(t1);
        timePicker.setMinute(t2);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        int time;
        if (restoreValue) {
            time = getPersistedInt(0);
        } else {
            time = (Integer) defaultValue;
            persistInt(time);
        }
        t1 = time / 60;
        t2 = time % 60;
        setSummary(getSummary());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            t1 = timePicker.getHour();
            t2 = timePicker.getMinute();
            int time = 60 * t1 + t2;
            if (callChangeListener(time)) {
                persistInt(time);
                setSummary(getSummary());
            }
        }
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.format(t1, t2);
    }
}