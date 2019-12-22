package linusfessler.alarmtiles.stopwatch;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import linusfessler.alarmtiles.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

class StopwatchRepository {

    private final ExecutorService writeExecutor;
    private final StopwatchDao stopwatchDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Stopwatch> stopwatch;

    @Inject
    StopwatchRepository(final AppDatabase appDatabase) {
        this.writeExecutor = appDatabase.getWriteExecutor();
        this.stopwatchDao = appDatabase.stopwatchDao();

        this.stopwatch = this.stopwatchDao.select();
    }

    void updateStopwatch(final Stopwatch stopwatch) {
        this.writeExecutor.execute(() -> this.stopwatchDao.update(stopwatch));
    }
}
