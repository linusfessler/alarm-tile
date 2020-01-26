package linusfessler.alarmtiles.stopwatch;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import io.reactivex.Observable;
import linusfessler.alarmtiles.AppDatabase;

class StopwatchRepository {

    private final ExecutorService writeExecutor;
    private final StopwatchDao stopwatchDao;

    private final Observable<Stopwatch> stopwatchObservable;

    @Inject
    StopwatchRepository(final AppDatabase appDatabase) {
        this.writeExecutor = appDatabase.getWriteExecutor();
        this.stopwatchDao = appDatabase.stopwatchDao();

        // Don't emit null (only happens before database is populated at first app start)
        this.stopwatchObservable = this.stopwatchDao.select().skipWhile(Objects::isNull);
    }

    void update(final Stopwatch stopwatch) {
        this.writeExecutor.execute(() -> this.stopwatchDao.update(stopwatch));
    }

    Observable<Stopwatch> getStopwatch() {
        return this.stopwatchObservable;
    }
}
