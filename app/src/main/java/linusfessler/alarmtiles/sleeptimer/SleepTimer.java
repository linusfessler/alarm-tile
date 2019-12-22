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
//    private UUID workRequestId;
//    private Long originalVolume;

    // TODO: Add a constructor with sensible default values
    public SleepTimer() {
        this.duration = 10000;
    }


    void enable(/*final UUID workRequestId, final long originalVolume*/) {
        this.setEnabled(true);
        this.setStartTimeStamp(System.currentTimeMillis());
//        this.setWorkRequestId(workRequestId);
//        this.setOriginalVolume(originalVolume);
    }

    void disable() {
        this.setEnabled(false);
        this.setStartTimeStamp(null);
//        this.setWorkRequestId(null);
//        this.setOriginalVolume(null);
    }
}
