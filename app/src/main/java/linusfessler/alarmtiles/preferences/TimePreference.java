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

public abstract class TimePreference extends DialogPreference {

    protected TimePicker timePicker;
    protected int hours = 0;
    protected int minutes = 0;

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
    public abstract CharSequence getSummary();
}