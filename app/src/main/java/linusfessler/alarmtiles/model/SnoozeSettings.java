package linusfessler.alarmtiles.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SnoozeSettings {

    private final boolean snoozeEnabled;
    private final int snoozeHours;
    private final int snoozeMinutes;

    public SnoozeSettings(final boolean snoozeEnabled, final int snoozeHours, final int snoozeMinutes) {
        this.snoozeEnabled = snoozeEnabled;
        this.snoozeHours = snoozeHours;
        this.snoozeMinutes = snoozeMinutes;
    }

}
