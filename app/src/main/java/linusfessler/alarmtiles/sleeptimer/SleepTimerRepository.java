package linusfessler.alarmtiles.sleeptimer;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import linusfessler.alarmtiles.core.AppDatabase;

@Singleton
public class SleepTimerRepository {

    private final ExecutorService writeExecutor;
    private final SleepTimerDao sleepTimerDao;
    private final Observable<SleepTimer> sleepTimerObservable;

    @Inject
    public SleepTimerRepository(final AppDatabase appDatabase) {
        writeExecutor = appDatabase.getWriteExecutor();
        sleepTimerDao = appDatabase.sleepTimerDao();

        // Don't emit null (only happens before database is populated at first app start)
        sleepTimerObservable = sleepTimerDao.select().skipWhile(Objects::isNull);
    }

    public void update(final SleepTimer sleepTimer) {
        writeExecutor.execute(() -> sleepTimerDao.update(sleepTimer));
    }

    public Observable<SleepTimer> getSleepTimer() {
        return sleepTimerObservable;
    }
}
