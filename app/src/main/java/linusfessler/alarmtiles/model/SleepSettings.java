package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SleepSettings implements Serializable {

    private final boolean timerEnabled;
    private final int hours;
    private final int minutes;
    private final boolean enteringDoNotDisturb;
    private final boolean allowingPriorityNotifications;

    private SleepSettings(final boolean timerEnabled, final int hours, final int minutes, final boolean enteringDoNotDisturb, final boolean allowingPriorityNotifications) {
        this.timerEnabled = timerEnabled;
        this.hours = hours;
        this.minutes = minutes;
        this.enteringDoNotDisturb = enteringDoNotDisturb;
        this.allowingPriorityNotifications = allowingPriorityNotifications;
    }

}
