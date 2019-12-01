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
    private Long startTimeStamp;

    // TODO: Add a constructor with sensible default values
    public SleepTimer() {
        this.duration = 10000;
    }

    void toggle() {
        if (this.isEnabled()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    void enable() {
        this.setEnabled(true);
        this.setStartTimeStamp(System.currentTimeMillis());
    }

    void disable() {
        this.setEnabled(false);
        this.setStartTimeStamp(null);
    }
}
