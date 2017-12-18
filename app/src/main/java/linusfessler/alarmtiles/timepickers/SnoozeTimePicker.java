package linusfessler.alarmtiles.timepickers;

import android.content.Context;
import android.util.AttributeSet;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.SnoozeScheduler;

public class SnoozeTimePicker extends AnalogTimePicker {

    public SnoozeTimePicker(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected boolean isClock() {
        return false;
    }

    @Override
    protected int getPreferenceKeyId() {
        return R.string.pref_snooze_duration_key;
    }

    @Override
    protected Scheduler getScheduler() {
        return SnoozeScheduler.getInstance();
    }
}
