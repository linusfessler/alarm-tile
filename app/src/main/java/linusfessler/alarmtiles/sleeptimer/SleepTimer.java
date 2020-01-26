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
    private boolean cancelled;

    public static SleepTimer createDefault() {
        return new SleepTimer(false, null, null, false);
    }

    public SleepTimer(final Long id, final boolean enabled, final Long startTimestamp, final Long duration, final boolean cancelled) {
        this(enabled, startTimestamp, duration, cancelled);
        this.setId(id);
    }

    @Ignore
    private SleepTimer(final boolean enabled, final Long startTimestamp, final Long duration, final boolean cancelled) {
        this.setEnabled(enabled);
        this.setStartTimestamp(startTimestamp);
        this.setDuration(duration);
        this.setCancelled(cancelled);
    }

    void start(final long duration) {
        this.setEnabled(true);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setDuration(duration);
        this.setCancelled(false);
    }

    void cancel() {
        this.finish();
        this.setCancelled(true);
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
