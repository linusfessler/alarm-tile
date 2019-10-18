package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FallAsleepSettings implements Serializable {

    private final boolean timerEnabled;
    private final int hour;
    private final int minute;
    private final boolean slowlyFadingMusicOut;

    private FallAsleepSettings(final boolean timerEnabled, final int hour, final int minute, final boolean slowlyFadingMusicOut) {
        this.timerEnabled = timerEnabled;
        this.hour = hour;
        this.minute = minute;
        this.slowlyFadingMusicOut = slowlyFadingMusicOut;
    }

}
