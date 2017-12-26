package linusfessler.alarmtiles.services.tile;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.TimerScheduler;

public class TimerTileService extends SchedulerTileService {

    @Override
    protected Scheduler getScheduler() {
        return TimerScheduler.getInstance(this);
    }

    @Override
    protected int getEnabledIcon() {
        return R.drawable.ic_timer_on;
    }

    @Override
    protected int getDisabledIcon() {
        return R.drawable.ic_timer_off;
    }
}
