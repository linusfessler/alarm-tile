package linusfessler.alarmtiles.sleeptimer;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import linusfessler.alarmtiles.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
@ToString
public class SleepTimer {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @Embedded
    private SleepTimerConfig config;

    @Getter(AccessLevel.PUBLIC)
    private boolean enabled;

    private Integer originalVolume;
    private Long startTimestamp;
    private Long additionalTime;

    public static SleepTimer createDefault() {
        final SleepTimerConfig config = SleepTimerConfig.createDefault();
        return new SleepTimer(config, false, null, null, null);
    }

    public SleepTimer(final Long id, final SleepTimerConfig config, final boolean enabled, final Integer originalVolume, final Long startTimestamp, final Long additionalTime) {
        this(config, enabled, originalVolume, startTimestamp, additionalTime);
        this.setId(id);
    }

    @Ignore
    private SleepTimer(final SleepTimerConfig config, final boolean enabled, final Integer originalVolume, final Long startTimestamp, final Long additionalTime) {
        this.setConfig(config);
        this.setEnabled(enabled);
        this.setOriginalVolume(originalVolume);
        this.setStartTimestamp(startTimestamp);
        this.setAdditionalTime(additionalTime);
    }

    void start(final Integer originalVolume) {
        this.setEnabled(true);
        this.setOriginalVolume(originalVolume);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setAdditionalTime(0L);
    }

    void addAdditionalTime(final long millis) {
        Assert.isTrue(millis > 0, "Additional time must be positive.");
        this.setAdditionalTime(this.getAdditionalTime() + millis);
    }

    void reset() {
        this.setEnabled(false);
        this.setOriginalVolume(null);
        this.setStartTimestamp(null);
        this.setAdditionalTime(null);
    }

    long getMillisLeft() {
        final long millisElapsed = System.currentTimeMillis() - this.getStartTimestamp();
        return this.getConfig().getDuration() + this.getAdditionalTime() - millisElapsed;
    }

    private void setOriginalVolume(final Integer originalVolume) {
        Assert.isTrue(originalVolume == null || originalVolume >= 0, "Original volume must be null or non-negative when resetting volume.");
        this.originalVolume = originalVolume;
    }
}
