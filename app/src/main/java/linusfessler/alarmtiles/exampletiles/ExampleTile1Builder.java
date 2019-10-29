package linusfessler.alarmtiles.exampletiles;

import android.content.Context;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.GeneralSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.model.WakeUpSettings;

public class ExampleTile1Builder {

    private final Context context;

    public ExampleTile1Builder(final Context context) {
        this.context = context.getApplicationContext();
    }

    public AlarmTile build() {
        final String iconResourceName = context.getResources().getResourceEntryName(R.drawable.ic_alarm_24px);

        final GeneralSettings generalSettings = GeneralSettings.builder()
                .name(context.getResources().getString(R.string.example_tile_1))
                .iconResourceName(iconResourceName)
                .showingNotification(true)
                .build();

        final FallAsleepSettings fallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(30)
                .slowlyDecreasingVolume(true)
                .build();

        final SleepSettings sleepSettings = SleepSettings.builder()
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(false)
                .build();

        final WakeUpSettings wakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(true)
                .alarmHour(6)
                .alarmMinute(30)
                .timerEnabled(false)
                .timerHours(0)
                .timerMinutes(0)
                .snoozeEnabled(true)
                .snoozeHours(0)
                .snoozeMinutes(15)
                .volumeTimerEnabled(true)
                .volumeTimerHours(5)
                .volumeTimerMinutes(0)
                .dismissTimerEnabled(false)
                .dismissTimerHours(0)
                .dismissTimerMinutes(0)
                .vibrating(false)
                .turningOnFlashlight(false)
                .build();

        return new AlarmTile(generalSettings, fallAsleepSettings, sleepSettings, wakeUpSettings);
    }

}
