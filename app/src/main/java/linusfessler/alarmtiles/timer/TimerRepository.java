package linusfessler.alarmtiles.timer;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import linusfessler.alarmtiles.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

class TimerRepository {

    private final ExecutorService writeExecutor;
    private final TimerDao timerDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Timer> timer;

    TimerRepository(final Application application) {
        final AppDatabase db = AppDatabase.getInstance(application);
        this.writeExecutor = db.getWriteExecutor();
        this.timerDao = db.timerDao();

        this.timer = this.timerDao.select();
    }

    void updateTimer(final Timer timer) {
        this.writeExecutor.execute(() -> this.timerDao.update(timer));
    }
}