package linusfessler.alarmtiles.alarm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString
@EqualsAndHashCode
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private boolean enabled;
    private int hourOfDay;
    private int minuteOfHour;

    // TODO: Add a constructor with sensible default values
    public Alarm() {
        this.hourOfDay = 7;
        this.minuteOfHour = 0;
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
    }

    void disable() {
        this.setEnabled(false);
    }
}
