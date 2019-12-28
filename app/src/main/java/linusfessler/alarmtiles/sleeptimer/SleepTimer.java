package linusfessler.alarmtiles.sleeptimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode
public class SleepTimer {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private boolean enabled;
    private long duration;
    private boolean fading;
    private Integer originalVolume;
    private Long startTimestamp;
    private Long additionalTime;

    // TODO: Add a constructor with sensible default values
    public SleepTimer() {
        this.duration = 10000;
        this.fading = true;
    }

    void start(final Integer originalVolume) {
        this.setEnabled(true);
        this.setOriginalVolume(originalVolume);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setAdditionalTime(0L);
    }

    void addAdditionalTime(final long millis) {
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
        return this.getDuration() + this.getAdditionalTime() - millisElapsed;
    }
}
