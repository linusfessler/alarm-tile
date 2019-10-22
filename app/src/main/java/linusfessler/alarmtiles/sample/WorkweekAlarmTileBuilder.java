package linusfessler.alarmtiles.sample;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.GeneralSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.model.SnoozeSettings;
import linusfessler.alarmtiles.model.WakeUpSettings;

public class WorkweekAlarmTileBuilder {

    public AlarmTile build() {
        final GeneralSettings generalSettings = GeneralSettings.builder()
                .name("Workweek Alarm")
                .iconResourceId(R.drawable.ic_alarm_24px)
                .showingNotification(true)
                .graduallyIncreasingVolume(false)
                .vibrating(false)
                .turningOnFlashlight(false)
                .build();

        final FallAsleepSettings fallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(30)
                .slowlyFadingMusicOut(true)
                .build();

        final SleepSettings sleepSettings = SleepSettings.builder()
                .timerEnabled(false)
                .timerHours(0)
                .timerMinutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(false)
                .build();

        final WakeUpSettings wakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(true)
                .alarmHour(6)
                .alarmMinute(30)
                .build();

        final SnoozeSettings snoozeSettings = SnoozeSettings.builder()
                .snoozeEnabled(true)
                .snoozeHours(0)
                .snoozeMinutes(15)
                .build();

        return new AlarmTile(generalSettings, fallAsleepSettings, sleepSettings, wakeUpSettings, snoozeSettings);
    }

}
