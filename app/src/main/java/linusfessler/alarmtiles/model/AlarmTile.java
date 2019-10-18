package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmTile implements Serializable {

    private BasicSettings basicSettings;
    private FallAsleepSettings fallAsleepSettings;
    private SleepSettings sleepSettings;
    private WakeUpSettings wakeUpSettings;
    private SnoozeSettings snoozeSettings;
    private AdvancedSettings advancedSettings;

}