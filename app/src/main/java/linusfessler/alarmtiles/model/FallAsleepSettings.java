package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FallAsleepSettings implements Serializable {

    private final boolean timerEnabled;
    private final int timerHours;
    private final int timerMinutes;
    private final boolean slowlyFadingMusicOut;

    private FallAsleepSettings(final boolean timerEnabled, final int timerHours, final int timerMinutes, final boolean slowlyFadingMusicOut) {
        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
    }

}
