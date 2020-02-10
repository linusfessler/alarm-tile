package linusfessler.alarmtiles.sleeptimer;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.concurrent.TimeUnit;

import lombok.Value;

@Value
@Entity
public class SleepTimer {

    @PrimaryKey
    private final long id;
    private final boolean enabled;
    private final Long startTimestamp;
    private final double time;
    private final TimeUnit timeUnit;
    private final boolean decreasingVolume;

    @Ignore
    public static SleepTimer createDefault() {
        return new SleepTimer(
                0,
                false,
                null,
                30,
                TimeUnit.MINUTES,
                true
        );
    }

    public SleepTimer(final long id, final boolean enabled, final Long startTimestamp, final double time, final TimeUnit timeUnit, final boolean decreasingVolume) {
        this.id = id;
        this.enabled = enabled;
        this.startTimestamp = startTimestamp;
        this.time = time;
        this.timeUnit = timeUnit;
        this.decreasingVolume = decreasingVolume;
    }

    public SleepTimer prepareForStart(final long startTimestamp) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                startTimestamp,
                getTime(),
                getTimeUnit(),
                isDecreasingVolume()
        );
    }

    public SleepTimer start() {
        return new SleepTimer(
                getId(),
                true,
                getStartTimestamp(),
                getTime(),
                getTimeUnit(),
                isDecreasingVolume()
        );
    }

    public SleepTimer stop() {
        return new SleepTimer(
                getId(),
                false,
                null,
                getTime(),
                getTimeUnit(),
                isDecreasingVolume()
        );
    }

    public SleepTimer setTime(final double time) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                time,
                getTimeUnit(),
                isDecreasingVolume()
        );
    }

    public SleepTimer setTimeUnit(final TimeUnit timeUnit) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                getTime(),
                timeUnit,
                isDecreasingVolume()
        );
    }

    public SleepTimer setDecreasingVolume(final boolean decreasingVolume) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                getTime(),
                getTimeUnit(),
                decreasingVolume
        );
    }

    public long getDurationMillis() {
        switch (getTimeUnit()) {
            case HOURS:
                return (long) (getTime() * 60 * 60 * 1000);
            case MINUTES:
                return (long) (getTime() * 60 * 1000);
            case SECONDS:
                return (long) (getTime() * 1000);
            default:
                throw new IllegalStateException(String.format("Unhandled time unit %s.", timeUnit));
        }
    }

    public long getMillisLeft() {
        final long millisElapsed = System.currentTimeMillis() - getStartTimestamp();
        return getDurationMillis() - millisElapsed;
    }
}
