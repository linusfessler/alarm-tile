package linusfessler.alarmtiles.sleeptimer.model;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SleepTimerConfig {

    private long duration;
    private TimeUnit timeUnit;
    private boolean fadingVolume;
    private boolean resettingVolume;

    public static SleepTimerConfig createDefault() {
        final long duration = 30 * 60 * 1000L;
        final TimeUnit timeUnit = TimeUnit.MINUTES;
        final boolean fadingVolume = true;
        final boolean resettingVolume = false;

        return new SleepTimerConfig(duration, timeUnit, fadingVolume, resettingVolume);
    }

    public SleepTimerConfig(final long duration, final TimeUnit timeUnit, final boolean fadingVolume, final boolean resettingVolume) {
        this.setDuration(duration);
        this.setTimeUnit(timeUnit);
        this.setFadingVolume(fadingVolume);
        this.setResettingVolume(resettingVolume);
    }
}
