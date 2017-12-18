package linusfessler.alarmtiles.services.tile;

import android.support.annotation.RequiresApi;

import linusfessler.alarmtiles.schedulers.Scheduler;

@RequiresApi(24)
public abstract class SchedulerTileService extends TileService {

    protected abstract Scheduler getScheduler();

    @Override
    protected boolean isActive() {
        return getScheduler().isScheduled(this);
    }

    @Override
    protected boolean isUnavailable() {
        return getScheduler().alarmIsActive();
    }

    @Override
    protected void onEnable() {
        getScheduler().schedule(this);
    }

    @Override
    protected void onDisable() {
        getScheduler().dismiss(this);
    }
}
