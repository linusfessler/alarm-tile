package linusfessler.alarmtiles.timer;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.core.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

@Singleton
class TimerRepository {

    private final ExecutorService writeExecutor;
    private final TimerDao timerDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Timer> timer;

    @Inject
    TimerRepository(final AppDatabase appDatabase) {
        writeExecutor = appDatabase.getWriteExecutor();
        timerDao = appDatabase.timerDao();

        timer = timerDao.select();
    }

    void update(final Timer timer) {
        writeExecutor.execute(() -> timerDao.update(timer));
    }
}
