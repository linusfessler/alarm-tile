package linusfessler.alarmtiles.model;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class AlarmTile implements Serializable {

    private final BasicSettings basicSettings = new BasicSettings();
    private final FallAsleepSettings fallAsleepSettings = new FallAsleepSettings();
    private final SleepSettings sleepSettings = new SleepSettings();
    private final WakeUpSettings wakeUpSettings = new WakeUpSettings();
    private final SnoozeSettings snoozeSettings = new SnoozeSettings();
    private final AdvancedSettings advancedSettings = new AdvancedSettings();

}
