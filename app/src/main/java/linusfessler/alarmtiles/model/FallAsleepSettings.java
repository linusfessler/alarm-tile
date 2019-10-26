package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FallAsleepSettings implements Serializable {

    private boolean timerEnabled;
    private int timerHours;
    private int timerMinutes;
    private boolean slowlyDecreasingVolume;

    @Ignore
    public FallAsleepSettings() {
        setTimerEnabled(false);
        setTimerHours(0);
        setTimerMinutes(30);
        setSlowlyDecreasingVolume(false);
    }

    public FallAsleepSettings(final boolean timerEnabled, final int timerHours, final int timerMinutes, final boolean slowlyDecreasingVolume) {
        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;
        this.slowlyDecreasingVolume = slowlyDecreasingVolume;
    }

}
