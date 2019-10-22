package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SnoozeSettings {

    private boolean snoozeEnabled;
    private int snoozeHours;
    private int snoozeMinutes;

    @Ignore
    public SnoozeSettings() {
        setSnoozeEnabled(false);
        setSnoozeHours(0);
        setSnoozeMinutes(15);
    }

    public SnoozeSettings(final boolean snoozeEnabled, final int snoozeHours, final int snoozeMinutes) {
        this.snoozeEnabled = snoozeEnabled;
        this.snoozeHours = snoozeHours;
        this.snoozeMinutes = snoozeMinutes;
    }

}
