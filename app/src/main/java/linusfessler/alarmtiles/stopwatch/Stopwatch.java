package linusfessler.alarmtiles.stopwatch;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity
public class Stopwatch {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private boolean enabled;
    private Long startTimeStamp;

    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    private void enable() {
        this.setEnabled(true);
        this.setStartTimeStamp(System.currentTimeMillis());
    }

    private void disable() {
        this.setEnabled(false);
        this.setStartTimeStamp(null);
    }
}
