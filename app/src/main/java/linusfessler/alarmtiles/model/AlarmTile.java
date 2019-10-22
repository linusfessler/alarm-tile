package linusfessler.alarmtiles.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Entity
public class AlarmTile implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @Setter
    private long id;

    @NonNull
    @Embedded(prefix = "general_")
    private final GeneralSettings generalSettings;

    @NonNull
    @Embedded(prefix = "fall_asleep_")
    private final FallAsleepSettings fallAsleepSettings;

    @NonNull
    @Embedded(prefix = "sleep_")
    private final SleepSettings sleepSettings;

    @NonNull
    @Embedded(prefix = "wake_up_")
    private final WakeUpSettings wakeUpSettings;

    @NonNull
    @Embedded(prefix = "snooze_")
    private final SnoozeSettings snoozeSettings;

    public AlarmTile(final GeneralSettings generalSettings, final FallAsleepSettings fallAsleepSettings, final SleepSettings sleepSettings, final WakeUpSettings wakeUpSettings, final SnoozeSettings snoozeSettings) {
        this.generalSettings = generalSettings;
        this.fallAsleepSettings = fallAsleepSettings;
        this.sleepSettings = sleepSettings;
        this.wakeUpSettings = wakeUpSettings;
        this.snoozeSettings = snoozeSettings;
    }

}
