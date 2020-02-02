package linusfessler.alarmtiles.sleeptimer;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import io.reactivex.Observable;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimer;

public class SleepTimerRepository {

    private final ExecutorService writeExecutor;
    private final SleepTimerDao sleepTimerDao;
    private final Observable<SleepTimer> sleepTimerObservable;

    @Inject
    public SleepTimerRepository(final AppDatabase appDatabase) {
        this.writeExecutor = appDatabase.getWriteExecutor();
        this.sleepTimerDao = appDatabase.sleepTimerDao();

        // Don't emit null (only happens before database is populated at first app start)
        this.sleepTimerObservable = this.sleepTimerDao.select().skipWhile(Objects::isNull);
    }

    public void update(final SleepTimer sleepTimer) {
        this.writeExecutor.execute(() -> this.sleepTimerDao.update(sleepTimer));
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable;
    }
}
