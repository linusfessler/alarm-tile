package linusfessler.alarmtiles.stopwatch;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import linusfessler.alarmtiles.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

class StopwatchRepository {

    private final ExecutorService writeExecutor;
    private final StopwatchDao stopwatchDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Stopwatch> stopwatch;

    StopwatchRepository(final Application application) {
        final AppDatabase db = AppDatabase.getInstance(application);
        this.writeExecutor = db.getWriteExecutor();
        this.stopwatchDao = db.stopwatchDao();

        this.stopwatch = this.stopwatchDao.select();
    }

    void updateStopwatch(final Stopwatch stopwatch) {
        this.writeExecutor.execute(() -> this.stopwatchDao.update(stopwatch));
    }
}
