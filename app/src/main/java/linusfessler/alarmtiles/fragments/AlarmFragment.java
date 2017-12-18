package linusfessler.alarmtiles.fragments;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.AlarmScheduler;
import linusfessler.alarmtiles.schedulers.Scheduler;

public class AlarmFragment extends SchedulerFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alarm;
    }

    @Override
    protected int getPositiveIconId() {
        return R.drawable.ic_alarm_on;
    }

    @Override
    protected int getNegativeIconId() {
        return R.drawable.ic_alarm_off;
    }

    @Override
    protected int getPreferenceKeyId() {
        return R.string.pref_key_alarm_scheduled;
    }

    @Override
    protected Scheduler getScheduler() {
        return AlarmScheduler.getInstance();
    }
}