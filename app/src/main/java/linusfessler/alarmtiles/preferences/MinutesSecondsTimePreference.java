package linusfessler.alarmtiles.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import linusfessler.alarmtiles.utility.TimeFormatter;

public class MinutesSecondsTimePreference extends TimePreference {

    public MinutesSecondsTimePreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        timePicker.setIs24HourView(true);
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.formatMinutesSeconds(t1, t2);
    }
}