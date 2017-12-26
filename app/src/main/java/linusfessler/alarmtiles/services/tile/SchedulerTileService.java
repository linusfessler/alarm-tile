package linusfessler.alarmtiles.services.tile;

import android.support.annotation.RequiresApi;

import linusfessler.alarmtiles.schedulers.Scheduler;

@RequiresApi(24)
public abstract class SchedulerTileService extends TileService {

    protected abstract Scheduler getScheduler();

    @Override
    protected boolean isActive() {
        return getScheduler().isScheduled();
    }

    @Override
    protected void onEnable() {
        getScheduler().schedule();
    }

    @Override
    protected void onDisable() {
        getScheduler().dismiss();
    }
}
