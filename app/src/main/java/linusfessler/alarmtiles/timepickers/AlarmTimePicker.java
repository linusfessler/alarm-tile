package linusfessler.alarmtiles.timepickers;

import android.content.Context;
import android.util.AttributeSet;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.AlarmScheduler;
import linusfessler.alarmtiles.schedulers.Scheduler;

public class AlarmTimePicker extends AnalogTimePicker {

    public AlarmTimePicker(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected boolean isClock() {
        return true;
    }

    @Override
    protected int getPreferenceKeyId() {
        return R.string.pref_alarm_time_key;
    }

    @Override
    protected Scheduler getScheduler() {
        return AlarmScheduler.getInstance();
    }
}
