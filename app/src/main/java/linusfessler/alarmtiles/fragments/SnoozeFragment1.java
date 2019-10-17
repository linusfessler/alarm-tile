package linusfessler.alarmtiles.fragments;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.SnoozeScheduler;

public class SnoozeFragment1 extends SchedulerFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected int getPositiveIconId() {
        return R.drawable.ic_snooze_24px;
    }

    @Override
    protected int getNegativeIconId() {
        return R.drawable.ic_alarm_off_24px;
    }

    @Override
    protected int getTimeKeyId() {
        return R.string.pref_snooze_duration_key;
    }

    @Override
    protected int getScheduledKeyId() {
        return R.string.pref_snooze_scheduled_key;
    }

    @Override
    protected Scheduler getScheduler() {
        return SnoozeScheduler.getInstance(getContext());
    }
}