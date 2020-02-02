package linusfessler.alarmtiles.sleeptimer;

import dagger.Subcomponent;

@SleepTimerServiceScope
@Subcomponent(modules = {SleepTimerServiceModule.class})
public interface SleepTimerServiceComponent {

    void inject(SleepTimerNotificationService sleepTimerService);
}
