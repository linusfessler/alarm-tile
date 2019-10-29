package linusfessler.alarmtiles.exampletiles;

import android.content.Context;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.GeneralSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.model.WakeUpSettings;

public class ExampleTile3Builder {

    private final Context context;

    public ExampleTile3Builder(final Context context) {
        this.context = context.getApplicationContext();
    }

    public AlarmTile build() {
        final String iconResourceName = context.getResources().getResourceEntryName(R.drawable.ic_snooze_24px);

        final GeneralSettings generalSettings = GeneralSettings.builder()
                .name(context.getResources().getString(R.string.example_tile_3))
                .iconResourceName(iconResourceName)
                .showingNotification(true)
                .build();

        final FallAsleepSettings fallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(false)
                .timerHours(0)
                .timerMinutes(0)
                .slowlyDecreasingVolume(false)
                .build();

        final SleepSettings sleepSettings = SleepSettings.builder()
                .enteringDoNotDisturb(false)
                .allowingPriorityNotifications(false)
                .build();

        final WakeUpSettings wakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(false)
                .alarmHour(0)
                .alarmMinute(0)
                .timerEnabled(true)
                .timerHours(1)
                .timerMinutes(0)
                .snoozeEnabled(false)
                .snoozeHours(0)
                .snoozeMinutes(0)
                .volumeTimerEnabled(false)
                .volumeTimerHours(0)
                .volumeTimerMinutes(0)
                .dismissTimerEnabled(true)
                .dismissTimerHours(5)
                .dismissTimerMinutes(0)
                .vibrating(false)
                .turningOnFlashlight(false)
                .build();

        return new AlarmTile(generalSettings, fallAsleepSettings, sleepSettings, wakeUpSettings);
    }

}
