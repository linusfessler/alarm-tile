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
    private boolean turningDeviceOff;

    static SleepTimerConfig createDefault() {
        return new SleepTimerConfig(30 * 60 * 1000L, true, true, false);
    }

    public SleepTimerConfig(final long duration, final boolean fading, final boolean resettingVolume, final boolean turningDeviceOff) {
        this.setDuration(duration);
        this.setFading(fading);
        this.setResettingVolume(resettingVolume);
        this.setTurningDeviceOff(turningDeviceOff);
    }

    void setDuration(final long duration) {
        Assert.isTrue(duration >= 0, "Duration can not be non-negative.");
        this.duration = duration;
    }

    void setResettingVolume(final boolean resettingVolume) {
        if (resettingVolume) {
            Assert.isTrue(this.fading, "Resetting volume only makes sense when fading.");
        }
        this.resettingVolume = resettingVolume;
    }
}
