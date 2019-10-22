package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FallAsleepSettings {

    private boolean timerEnabled;
    private int timerHours;
    private int timerMinutes;
    private boolean slowlyFadingMusicOut;

    @Ignore
    public FallAsleepSettings() {
        setTimerEnabled(false);
        setTimerHours(0);
        setTimerMinutes(30);
        setSlowlyFadingMusicOut(false);
    }

    public FallAsleepSettings(final boolean timerEnabled, final int timerHours, final int timerMinutes, final boolean slowlyFadingMusicOut) {
        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
    }

}
