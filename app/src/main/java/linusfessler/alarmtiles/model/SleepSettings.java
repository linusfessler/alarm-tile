package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SleepSettings {

    private boolean timerEnabled;
    private int timerHours;
    private int timerMinutes;
    private boolean enteringDoNotDisturb;
    private boolean allowingPriorityNotifications;

    @Ignore
    public SleepSettings() {
        setTimerEnabled(false);
        setTimerHours(8);
        setTimerMinutes(0);
        setEnteringDoNotDisturb(false);
        setAllowingPriorityNotifications(false);
    }

    public SleepSettings(final boolean timerEnabled, final int timerHours, final int timerMinutes, final boolean enteringDoNotDisturb, final boolean allowingPriorityNotifications) {
        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;
        this.enteringDoNotDisturb = enteringDoNotDisturb;
        this.allowingPriorityNotifications = allowingPriorityNotifications;
    }

}
