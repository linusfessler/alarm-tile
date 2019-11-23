package linusfessler.alarmtiles.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity
public class TimerTile {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    public TimerTile() {

    }
}
