package linusfessler.alarmtiles.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.utility.TimeFormatter;

public class DigitalTimePreference extends TimePreference {

    public DigitalTimePreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected View onCreateDialogView() {
        timePicker = (TimePicker) LayoutInflater.from(getContext()).inflate(R.layout.time_dialog, null);
        timePicker.setIs24HourView(true);
        return timePicker;
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.formatHoursMinutes(hours, minutes);
    }
}