package linusfessler.alarmtiles;

import javax.inject.Singleton;

import dagger.Component;
import linusfessler.alarmtiles.alarm.AlarmTileService;
import linusfessler.alarmtiles.fragments.MainFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerServiceComponent;
import linusfessler.alarmtiles.sleeptimer.SleepTimerServiceModule;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;
import linusfessler.alarmtiles.stopwatch.StopwatchTileService;
import linusfessler.alarmtiles.timer.TimerTileService;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    SleepTimerServiceComponent sleepTimerServiceComponent(SleepTimerServiceModule sleepTimerServiceModule);

    void inject(MainFragment mainFragment);

    void inject(SleepTimerTileService sleepTimerTileService);

    void inject(AlarmTileService alarmTileService);

    void inject(TimerTileService timerTileService);

    void inject(StopwatchTileService stopwatchTileService);
}
