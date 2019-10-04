package linusfessler.alarmtiles.services.tile;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.schedulers.AlarmScheduler;
import linusfessler.alarmtiles.schedulers.Scheduler;

public class AlarmTileService extends SchedulerTileService {

    @Override
    protected Scheduler getScheduler() {
        return AlarmScheduler.getInstance(this);
    }

    @Override
    protected int getEnabledIcon() {
        return R.drawable.ic_alarm_24px;
    }

    @Override
    protected int getDisabledIcon() {
        return R.drawable.ic_alarm_off_24px;
    }
}

