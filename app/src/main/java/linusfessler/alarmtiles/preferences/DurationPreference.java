package linusfessler.alarmtiles.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.utility.TimeFormatter;

public class DurationPreference extends TimePreference {

    public DurationPreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        timePicker.setIs24HourView(true);
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.formatHoursMinutes(hours, minutes);
    }
}