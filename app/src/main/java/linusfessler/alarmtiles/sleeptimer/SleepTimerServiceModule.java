package linusfessler.alarmtiles.sleeptimer;


import androidx.lifecycle.Lifecycle;

import dagger.Module;
import dagger.Provides;

@Module
public class SleepTimerServiceModule {

    private final SleepTimerService sleepTimerService;

    public SleepTimerServiceModule(final SleepTimerService sleepTimerService) {
        this.sleepTimerService = sleepTimerService;
    }

    @Provides
    SleepTimerService sleepTimerService() {
        return this.sleepTimerService;
    }

    @Provides
    Lifecycle lifecycle() {
        return this.sleepTimerService.getLifecycle();
    }
}