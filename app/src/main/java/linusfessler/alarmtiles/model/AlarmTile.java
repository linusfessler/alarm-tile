package linusfessler.alarmtiles.model;

import android.content.Context;

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

    @Ignore
    public AlarmTile(final Context context) {
        this.generalSettings = new GeneralSettings(context);
        this.fallAsleepSettings = new FallAsleepSettings();
        this.sleepSettings = new SleepSettings();
        this.wakeUpSettings = new WakeUpSettings();
    }

    public AlarmTile(final GeneralSettings generalSettings, final FallAsleepSettings fallAsleepSettings, final SleepSettings sleepSettings, final WakeUpSettings wakeUpSettings) {
        this.generalSettings = generalSettings;
        this.fallAsleepSettings = fallAsleepSettings;
        this.sleepSettings = sleepSettings;
        this.wakeUpSettings = wakeUpSettings;
    }

}
