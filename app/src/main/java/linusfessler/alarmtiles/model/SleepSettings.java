package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SleepSettings implements Serializable {

    private boolean enteringDoNotDisturb;
    private boolean allowingPriorityNotifications;

    @Ignore
    public SleepSettings() {
        setEnteringDoNotDisturb(false);
        setAllowingPriorityNotifications(false);
    }

    public SleepSettings(final boolean enteringDoNotDisturb, final boolean allowingPriorityNotifications) {
        this.enteringDoNotDisturb = enteringDoNotDisturb;
        this.allowingPriorityNotifications = allowingPriorityNotifications;
    }

}
