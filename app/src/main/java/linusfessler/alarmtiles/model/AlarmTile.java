package linusfessler.alarmtiles.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Data;

@Data
@Entity
public class AlarmTile implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @Embedded(prefix = "general_")
    private GeneralSettings generalSettings;

    @Embedded(prefix = "fall_asleep_")
    private FallAsleepSettings fallAsleepSettings;

    @Embedded(prefix = "sleep_")
    private SleepSettings sleepSettings;

    @Embedded(prefix = "wake_up_")
    private WakeUpSettings wakeUpSettings;

    @Embedded(prefix = "snooze_")
    private SnoozeSettings snoozeSettings;

    @Ignore
    public AlarmTile() {
        this.generalSettings = new GeneralSettings();
        this.fallAsleepSettings = new FallAsleepSettings();
        this.sleepSettings = new SleepSettings();
        this.wakeUpSettings = new WakeUpSettings();
        this.snoozeSettings = new SnoozeSettings();
    }

    public AlarmTile(final GeneralSettings generalSettings, final FallAsleepSettings fallAsleepSettings, final SleepSettings sleepSettings, final WakeUpSettings wakeUpSettings, final SnoozeSettings snoozeSettings) {
        this.generalSettings = generalSettings;
        this.fallAsleepSettings = fallAsleepSettings;
        this.sleepSettings = sleepSettings;
        this.wakeUpSettings = wakeUpSettings;
        this.snoozeSettings = snoozeSettings;
    }

}
