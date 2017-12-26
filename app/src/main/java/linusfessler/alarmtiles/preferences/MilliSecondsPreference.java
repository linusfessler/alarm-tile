package linusfessler.alarmtiles.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

import linusfessler.alarmtiles.utility.TimeFormatter;

public class MilliSecondsPreference extends EditTextPreference {

    public MilliSecondsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.formatMilliSeconds(getText());
    }
}