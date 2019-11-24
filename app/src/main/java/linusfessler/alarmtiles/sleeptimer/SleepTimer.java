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

    public SleepTimer() {
        this.enabled = false;
    }
}
