package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FallAsleepSettings implements Serializable {

    private final boolean timerEnabled;
    private final int hours;
    private final int minutes;
    private final boolean slowlyFadingMusicOut;

    private FallAsleepSettings(final boolean timerEnabled, final int hours, final int minutes, final boolean slowlyFadingMusicOut) {
        this.timerEnabled = timerEnabled;
        this.hours = hours;
        this.minutes = minutes;
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
    }

}
