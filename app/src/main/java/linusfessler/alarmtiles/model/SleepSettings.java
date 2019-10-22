package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SleepSettings implements Serializable {

    private final boolean timerEnabled;
    private final int timerHours;
    private final int timerMinutes;
    private final boolean enteringDoNotDisturb;
    private final boolean allowingPriorityNotifications;

    public SleepSettings(final boolean timerEnabled, final int timerHours, final int timerMinutes, final boolean enteringDoNotDisturb, final boolean allowingPriorityNotifications) {
        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;
        this.enteringDoNotDisturb = enteringDoNotDisturb;
        this.allowingPriorityNotifications = allowingPriorityNotifications;
    }

}
