package linusfessler.alarmtiles.core;

import javax.inject.Singleton;

import dagger.Component;
import linusfessler.alarmtiles.alarm.AlarmFragment;
import linusfessler.alarmtiles.alarm.AlarmTileService;
import linusfessler.alarmtiles.sleeptimer.SleepTimerConfigFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerNotificationService;
import linusfessler.alarmtiles.sleeptimer.SleepTimerTileService;
import linusfessler.alarmtiles.stopwatch.StopwatchFragment;
import linusfessler.alarmtiles.stopwatch.StopwatchTileService;
import linusfessler.alarmtiles.timer.TimerFragment;
import linusfessler.alarmtiles.timer.TimerTileService;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(SleepTimerConfigFragment sleepTimerConfigFragment);

    void inject(SleepTimerTileService sleepTimerTileService);

    void inject(SleepTimerNotificationService sleepTimerNotificationService);

    void inject(SleepTimerFragment sleepTimerFragment);

    void inject(AlarmFragment alarmFragment);

    void inject(TimerFragment timerFragment);

    void inject(StopwatchFragment stopwatchFragment);

    void inject(AlarmTileService alarmTileService);

    void inject(TimerTileService timerTileService);

    void inject(StopwatchTileService stopwatchTileService);
}
