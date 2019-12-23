package linusfessler.alarmtiles.sleeptimer;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import lombok.AccessLevel;
import lombok.Getter;

@Singleton
public class SleepTimerService {

    private final SleepTimerWorkCoordinator workCoordinator;

    @Getter(AccessLevel.PACKAGE)
    private final Observable<SleepTimer> sleepTimer;

    @Inject
    public SleepTimerService(final SleepTimerRepository repository, final SleepTimerWorkCoordinator workCoordinator) {
        this.workCoordinator = workCoordinator;
        this.sleepTimer = repository.getSleepTimer();
    }

    void toggleSleepTimer(final SleepTimer sleepTimer) {
        if (sleepTimer.isEnabled()) {
            this.workCoordinator.stop();
        } else {
            this.workCoordinator.start();
        }
    }
}
