package linusfessler.alarmtiles;

import javax.inject.Singleton;

import dagger.Component;
import linusfessler.alarmtiles.alarm.AlarmTileService;
import linusfessler.alarmtiles.fragments.MainFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerProgressWorker;
import linusfessler.alarmtiles.sleeptimer.SleepTimerStartWorker;
import linusfessler.alarmtiles.sleeptimer.SleepTimerStopWorker;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;
import linusfessler.alarmtiles.stopwatch.StopwatchTileService;
import linusfessler.alarmtiles.timer.TimerTileService;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainFragment mainFragment);

    void inject(SleepTimerTileService sleepTimerTileService);

    void inject(AlarmTileService alarmTileService);

    void inject(TimerTileService timerTileService);

    void inject(StopwatchTileService stopwatchTileService);

    void inject(SleepTimerStartWorker sleepTimerStartWorker);

    void inject(SleepTimerProgressWorker sleepTimerProgressWorker);

    void inject(SleepTimerStopWorker sleepTimerStopWorker);
}
