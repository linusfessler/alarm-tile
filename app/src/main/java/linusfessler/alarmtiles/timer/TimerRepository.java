package linusfessler.alarmtiles.timer;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import linusfessler.alarmtiles.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

class TimerRepository {

    private final ExecutorService writeExecutor;
    private final TimerDao timerDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Timer> timer;

    @Inject
    TimerRepository(final AppDatabase appDatabase) {
        this.writeExecutor = appDatabase.getWriteExecutor();
        this.timerDao = appDatabase.timerDao();

        this.timer = this.timerDao.select();
    }

    void updateTimer(final Timer timer) {
        this.writeExecutor.execute(() -> this.timerDao.update(timer));
    }
}
