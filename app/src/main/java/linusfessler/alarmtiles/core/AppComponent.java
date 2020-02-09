package linusfessler.alarmtiles.core;

import javax.inject.Singleton;

import dagger.Component;
import linusfessler.alarmtiles.alarm.AlarmTileService;
import linusfessler.alarmtiles.sleeptimer.SleepTimerConfigFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerNotificationService;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;
import linusfessler.alarmtiles.stopwatch.StopwatchTileService;
import linusfessler.alarmtiles.timer.TimerTileService;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainFragment mainFragment);

    void inject(SleepTimerConfigFragment sleepTimerConfigFragment);

    void inject(SleepTimerTileService sleepTimerTileService);

    void inject(SleepTimerNotificationService sleepTimerNotificationService);

    void inject(AlarmTileService alarmTileService);

    void inject(TimerTileService timerTileService);

    void inject(StopwatchTileService stopwatchTileService);
}
