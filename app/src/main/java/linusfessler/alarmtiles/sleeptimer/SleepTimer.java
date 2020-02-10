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
    private final Integer originalVolume;
    private final double time;
    private final TimeUnit timeUnit;
    private final boolean decreasingVolume;
    private final boolean resettingVolume;

    @Ignore
    public static SleepTimer createDefault() {
        return new SleepTimer(
                0,
                false,
                null,
                null,
                30,
                TimeUnit.MINUTES,
                true,
                false
        );
    }

    public SleepTimer(final long id, final boolean enabled, final Long startTimestamp, final Integer originalVolume, final double time, final TimeUnit timeUnit, final boolean decreasingVolume, final boolean resettingVolume) {
        this.id = id;
        this.enabled = enabled;
        this.startTimestamp = startTimestamp;
        this.originalVolume = originalVolume;
        this.time = time;
        this.timeUnit = timeUnit;
        this.decreasingVolume = decreasingVolume;
        this.resettingVolume = resettingVolume;
    }

    public SleepTimer prepareForStart(final long startTimestamp, final int originalVolume) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                startTimestamp,
                originalVolume,
                getTime(),
                getTimeUnit(),
                isDecreasingVolume(),
                isResettingVolume()
        );
    }

    public SleepTimer start() {
        return new SleepTimer(
                getId(),
                true,
                getStartTimestamp(),
                getOriginalVolume(),
                getTime(),
                getTimeUnit(),
                isDecreasingVolume(),
                isResettingVolume()
        );
    }

    public SleepTimer stop() {
        return new SleepTimer(
                getId(),
                false,
                null,
                null,
                getTime(),
                getTimeUnit(),
                isDecreasingVolume(),
                isResettingVolume()
        );
    }

    public SleepTimer setTime(final double time) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                getOriginalVolume(),
                time,
                getTimeUnit(),
                isDecreasingVolume(),
                isResettingVolume()
        );
    }

    public SleepTimer setTimeUnit(final TimeUnit timeUnit) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                getOriginalVolume(),
                getTime(),
                timeUnit,
                isDecreasingVolume(),
                isResettingVolume()
        );
    }

    public SleepTimer setDecreasingVolume(final boolean decreasingVolume) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                getOriginalVolume(),
                getTime(),
                getTimeUnit(),
                decreasingVolume,
                isResettingVolume()
        );
    }

    public SleepTimer setResettingVolume(final boolean resettingVolume) {
        return new SleepTimer(
                getId(),
                isEnabled(),
                getStartTimestamp(),
                getOriginalVolume(),
                getTime(),
                getTimeUnit(),
                isDecreasingVolume(),
                resettingVolume
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

    public boolean shouldResetVolume() {
        return isDecreasingVolume() && isResettingVolume();
    }
}
