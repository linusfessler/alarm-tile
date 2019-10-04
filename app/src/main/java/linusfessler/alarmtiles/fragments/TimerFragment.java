package linusfessler.alarmtiles.fragments;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.TimerScheduler;

public class TimerFragment extends SchedulerFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected int getPositiveIconId() {
        return R.drawable.ic_timer_24px;
    }

    @Override
    protected int getNegativeIconId() {
        return R.drawable.ic_timer_off_24px;
    }

    @Override
    protected int getTimeKeyId() {
        return R.string.pref_timer_duration_key;
    }

    @Override
    protected int getScheduledKeyId() {
        return R.string.pref_timer_scheduled_key;
    }

    @Override
    protected Scheduler getScheduler() {
        return TimerScheduler.getInstance(getContext());
    }
}