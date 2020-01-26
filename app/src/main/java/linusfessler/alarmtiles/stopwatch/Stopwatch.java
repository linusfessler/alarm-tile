package linusfessler.alarmtiles.stopwatch;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@ToString
public class Stopwatch {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @Getter(AccessLevel.PUBLIC)
    private boolean enabled;
    private Long startTimestamp;
    private Long stopTimestamp;

    public static Stopwatch createDefault() {
        return new Stopwatch(false, null, null);
    }

    public Stopwatch(final Long id, final boolean enabled, final Long startTimestamp, final Long stopTimestamp) {
        this(enabled, startTimestamp, stopTimestamp);
        this.setId(id);
    }

    @Ignore
    private Stopwatch(final boolean enabled, final Long startTimestamp, final Long stopTimestamp) {
        this.setEnabled(enabled);
        this.setStartTimestamp(startTimestamp);
        this.setStopTimestamp(stopTimestamp);
    }

    void toggle() {
        if (this.isEnabled()) {
            this.stop();
        } else {
            this.start();
        }
    }

    private void start() {
        this.setEnabled(true);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setStopTimestamp(null);
    }

    private void stop() {
        this.setEnabled(false);
        this.setStopTimestamp(System.currentTimeMillis());
    }
}
