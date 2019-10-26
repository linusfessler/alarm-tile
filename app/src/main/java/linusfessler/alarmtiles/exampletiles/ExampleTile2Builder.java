package linusfessler.alarmtiles.exampletiles;

import android.content.Context;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.GeneralSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.model.SnoozeSettings;
import linusfessler.alarmtiles.model.WakeUpSettings;

public class ExampleTile2Builder {

    private final Context context;

    public ExampleTile2Builder(final Context context) {
        this.context = context.getApplicationContext();
    }

    public AlarmTile build() {
        final GeneralSettings generalSettings = GeneralSettings.builder()
                .name(context.getResources().getString(R.string.example_tile_2))
                .iconResourceId(R.drawable.ic_timer_24px)
                .showingNotification(true)
                .volumeTimerEnabled(true)
                .volumeTimerHours(10)
                .volumeTimerMinutes(0)
                .dismissTimerEnabled(false)
                .dismissTimerHours(0)
                .dismissTimerMinutes(0)
                .vibrating(false)
                .turningOnFlashlight(false)
                .build();

        final FallAsleepSettings fallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(45)
                .slowlyDecreasingVolume(true)
                .build();

        final SleepSettings sleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .timerHours(9)
                .timerMinutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();

        final WakeUpSettings wakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(false)
                .alarmHour(0)
                .alarmMinute(0)
                .build();

        final SnoozeSettings snoozeSettings = SnoozeSettings.builder()
                .snoozeEnabled(true)
                .snoozeHours(0)
                .snoozeMinutes(30)
                .build();

        return new AlarmTile(generalSettings, fallAsleepSettings, sleepSettings, wakeUpSettings, snoozeSettings);
    }

}
