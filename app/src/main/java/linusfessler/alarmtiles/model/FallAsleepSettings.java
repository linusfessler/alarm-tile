package linusfessler.alarmtiles.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FallAsleepSettings {

    private final boolean timerEnabled;
    private final int timerHours;
    private final int timerMinutes;
    private final boolean slowlyFadingMusicOut;

    public FallAsleepSettings(final boolean timerEnabled, final int timerHours, final int timerMinutes, final boolean slowlyFadingMusicOut) {
        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
    }

}
