package linusfessler.alarmtiles.fragments;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.AlarmScheduler;
import linusfessler.alarmtiles.schedulers.Scheduler;

public class AlarmFragment extends SchedulerFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected int getPositiveIconId() {
        return R.drawable.ic_alarm_24px;
    }

    @Override
    protected int getNegativeIconId() {
        return R.drawable.ic_alarm_off_24px;
    }

    @Override
    protected int getTimeKeyId() {
        return R.string.pref_alarm_time_key;
    }

    @Override
    protected int getScheduledKeyId() {
        return R.string.pref_alarm_scheduled_key;
    }

    @Override
    protected Scheduler getScheduler() {
        return AlarmScheduler.getInstance(getContext());
    }
}