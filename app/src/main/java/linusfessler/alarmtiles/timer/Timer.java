package linusfessler.alarmtiles.timer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity
public class Timer {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private boolean enabled;
    private long duration;
    private Long startTimeStamp;

    // TODO: Add a constructor with sensible default values
    public Timer() {
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
