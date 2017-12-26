package linusfessler.alarmtiles.services.tile;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.Scheduler;
import linusfessler.alarmtiles.schedulers.SnoozeScheduler;

public class SnoozeTileService extends SchedulerTileService {

    @Override
    protected Scheduler getScheduler() {
        return SnoozeScheduler.getInstance(this);
    }

    @Override
    protected int getEnabledIcon() {
        return R.drawable.ic_snooze_on;
    }

    @Override
    protected int getDisabledIcon() {
        return R.drawable.ic_alarm_off;
    }
}
