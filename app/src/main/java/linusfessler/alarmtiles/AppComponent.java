package linusfessler.alarmtiles;

import javax.inject.Singleton;

import dagger.Component;
import linusfessler.alarmtiles.alarm.AlarmTileService;
import linusfessler.alarmtiles.fragments.MainFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerConfigFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerService;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;
import linusfessler.alarmtiles.stopwatch.StopwatchTileService;
import linusfessler.alarmtiles.timer.TimerTileService;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainFragment mainFragment);

    void inject(SleepTimerConfigFragment sleepTimerConfigFragment);

    void inject(SleepTimerTileService sleepTimerTileService);

    void inject(SleepTimerService sleepTimerService);

    void inject(AlarmTileService alarmTileService);

    void inject(TimerTileService timerTileService);

    void inject(StopwatchTileService stopwatchTileService);
}
