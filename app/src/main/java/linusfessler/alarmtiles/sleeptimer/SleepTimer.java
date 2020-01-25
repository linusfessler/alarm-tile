package linusfessler.alarmtiles.sleeptimer;

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
public class SleepTimer {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @Getter(AccessLevel.PUBLIC)
    private boolean enabled;
    private Long startTimestamp;
    private Long duration;

    public static SleepTimer createDefault() {
        return new SleepTimer(false, null, null);
    }

    public SleepTimer(final Long id, final boolean enabled, final Long startTimestamp, final Long duration) {
        this(enabled, startTimestamp, duration);
        this.setId(id);
    }

    @Ignore
    private SleepTimer(final boolean enabled, final Long startTimestamp, final Long duration) {
        this.setEnabled(enabled);
        this.setStartTimestamp(startTimestamp);
        this.setDuration(duration);
    }

    void start(final long duration) {
        this.setEnabled(true);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setDuration(duration);
    }

    void cancel() {
        this.finish();
    }

    void finish() {
        this.setEnabled(false);
        this.setStartTimestamp(null);
        this.setDuration(null);
    }

    long getMillisLeft() {
        final long millisElapsed = System.currentTimeMillis() - this.getStartTimestamp();
        return this.getDuration() - millisElapsed;
    }
}
