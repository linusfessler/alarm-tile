package linusfessler.alarmtiles.sleeptimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity
public class SleepTimer {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private boolean enabled;
    private long duration;
    private boolean fading;
    private Long startTimestamp;
    private Integer originalVolume;

    // TODO: Add a constructor with sensible default values
    public SleepTimer() {
        this.duration = 10000;
        this.fading = true;
    }

    void start(final Integer originalVolume) {
        this.setEnabled(true);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setOriginalVolume(originalVolume);
    }

    void stop() {
        this.setEnabled(false);
        this.setStartTimestamp(null);
        this.setOriginalVolume(null);
    }
}
