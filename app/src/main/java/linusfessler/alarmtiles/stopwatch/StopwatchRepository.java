package linusfessler.alarmtiles.stopwatch;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import linusfessler.alarmtiles.core.AppDatabase;

@Singleton
class StopwatchRepository {

    private final ExecutorService writeExecutor;
    private final StopwatchDao stopwatchDao;

    private final Observable<Stopwatch> stopwatchObservable;

    @Inject
    StopwatchRepository(final AppDatabase appDatabase) {
        writeExecutor = appDatabase.getWriteExecutor();
        stopwatchDao = appDatabase.stopwatchDao();

        // Don't emit null (only happens before database is populated at first app start)
        stopwatchObservable = stopwatchDao.select().skipWhile(Objects::isNull);
    }

    void update(final Stopwatch stopwatch) {
        writeExecutor.execute(() -> stopwatchDao.update(stopwatch));
    }

    Observable<Stopwatch> getStopwatch() {
        return stopwatchObservable;
    }
}
