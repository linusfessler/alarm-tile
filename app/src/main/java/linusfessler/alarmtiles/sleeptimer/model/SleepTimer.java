package linusfessler.alarmtiles.sleeptimer.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import linusfessler.alarmtiles.sleeptimer.SleepTimerState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
public class SleepTimer {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @Embedded(prefix = "config")
    private SleepTimerConfig config;

    private SleepTimerState state;
    private Long startTimestamp;
    private Integer originalVolume;

    public static SleepTimer createDefault() {
        final SleepTimerConfig config = SleepTimerConfig.createDefault();
        final SleepTimerState state = SleepTimerState.FINISHED;
        final Long startTimestamp = null;
        final Integer originalVolume = null;

        return new SleepTimer(config, state, startTimestamp, originalVolume);
    }

    public SleepTimer(final Long id, final SleepTimerConfig config, final SleepTimerState state, final Long startTimestamp, final Integer originalVolume) {
        this(config, state, startTimestamp, originalVolume);
        this.setId(id);
    }

    @Ignore
    private SleepTimer(final SleepTimerConfig config, final SleepTimerState state, final Long startTimestamp, final Integer originalVolume) {
        this.setConfig(config);
        this.setState(state);
        this.setStartTimestamp(startTimestamp);
        this.setOriginalVolume(originalVolume);
    }

    public void start(final int originalVolume) {
        this.setState(SleepTimerState.RUNNING);
        this.setStartTimestamp(System.currentTimeMillis());
        this.setOriginalVolume(originalVolume);
    }

    public void cancel() {
        this.setState(SleepTimerState.CANCELLED);
        this.setStartTimestamp(null);
        this.setOriginalVolume(null);
    }

    public void finish() {
        this.setState(SleepTimerState.FINISHED);
        this.setStartTimestamp(null);
        this.setOriginalVolume(null);
    }

    public long getMillisLeft() {
        final long millisElapsed = System.currentTimeMillis() - this.getStartTimestamp();
        return this.getConfig().getDuration() - millisElapsed;
    }
}
