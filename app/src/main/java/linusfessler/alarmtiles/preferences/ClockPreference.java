package linusfessler.alarmtiles.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import linusfessler.alarmtiles.utility.TimeFormatter;

public class ClockPreference extends TimePreference {

    public ClockPreference(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.format(hours, minutes);
    }
}