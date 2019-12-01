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
}
