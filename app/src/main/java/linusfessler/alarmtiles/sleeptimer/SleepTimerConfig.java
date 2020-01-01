package linusfessler.alarmtiles.sleeptimer;

import androidx.room.Entity;

import linusfessler.alarmtiles.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@ToString
class SleepTimerConfig {

    private long duration;
    private boolean fading;
    private boolean resettingVolume;

    static SleepTimerConfig createDefault() {
        return new SleepTimerConfig(30 * 60 * 1000L, true, true);
    }

    SleepTimerConfig(final long duration, final boolean fading, final boolean resettingVolume) {
        this.setDuration(duration);
        this.setFading(fading);
        this.setResettingVolume(resettingVolume);
    }

    void setDuration(final long duration) {
        Assert.isTrue(duration >= 0, "Duration must be non-negative.");
        this.duration = duration;
    }

    boolean shouldResetVolume() {
        return this.isFading() && this.isResettingVolume();
    }
}
