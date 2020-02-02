package linusfessler.alarmtiles.sleeptimer;

import androidx.lifecycle.Lifecycle;

import dagger.Module;
import dagger.Provides;

@Module
public class SleepTimerServiceModule {

    private final SleepTimerNotificationService sleepTimerService;

    public SleepTimerServiceModule(final SleepTimerNotificationService sleepTimerService) {
        this.sleepTimerService = sleepTimerService;
    }

    @Provides
    SleepTimerNotificationService sleepTimerService() {
        return this.sleepTimerService;
    }

    @Provides
    Lifecycle lifecycle() {
        return this.sleepTimerService.getLifecycle();
    }
}