package linusfessler.alarmtiles.timepickers;

import android.content.Context;
import android.util.AttributeSet;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.TimerScheduler;

public class TimerTimePicker extends AnalogTimePicker {

    public TimerTimePicker(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected boolean isClock() {
        return false;
    }

    @Override
    protected int getPreferenceKeyId() {
        return R.string.pref_timer_duration_key;
    }

    @Override
    protected Scheduler getScheduler() {
        return TimerScheduler.getInstance();
    }
}
