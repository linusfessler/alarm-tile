package linusfessler.alarmtiles.sleeptimer;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import linusfessler.alarmtiles.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

@Singleton
class SleepTimerRepository {

    private final ExecutorService writeExecutor;
    private final SleepTimerDao sleepTimerDao;

    @Getter(AccessLevel.PACKAGE)
    private final Observable<SleepTimer> sleepTimer;

    @Inject
    SleepTimerRepository(final AppDatabase appDatabase) {
        this.writeExecutor = appDatabase.getWriteExecutor();
        this.sleepTimerDao = appDatabase.sleepTimerDao();

        this.sleepTimer = this.sleepTimerDao.select();
    }

    void updateSleepTimer(final SleepTimer sleepTimer) {
        this.writeExecutor.execute(() -> this.sleepTimerDao.update(sleepTimer));
    }
}
