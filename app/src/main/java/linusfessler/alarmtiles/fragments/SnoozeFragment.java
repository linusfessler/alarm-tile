package linusfessler.alarmtiles.fragments;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.SnoozeScheduler;

public class SnoozeFragment extends SchedulerFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_snooze;
    }

    @Override
    protected int getPositiveIconId() {
        return R.drawable.ic_snooze_on;
    }

    @Override
    protected int getNegativeIconId() {
        return R.drawable.ic_alarm_off;
    }

    @Override
    protected int getPreferenceKeyId() {
        return R.string.pref_key_snooze_scheduled;
    }

    @Override
    protected Scheduler getScheduler() {
        return SnoozeScheduler.getInstance();
    }
}