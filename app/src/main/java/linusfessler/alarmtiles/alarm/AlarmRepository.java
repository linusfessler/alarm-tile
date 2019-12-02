package linusfessler.alarmtiles.alarm;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import linusfessler.alarmtiles.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

class AlarmRepository {

    private final ExecutorService writeExecutor;
    private final AlarmDao alarmDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Alarm> alarm;

    AlarmRepository(final Application application) {
        final AppDatabase db = AppDatabase.getInstance(application);
        this.writeExecutor = db.getWriteExecutor();
        this.alarmDao = db.alarmDao();

        this.alarm = this.alarmDao.select();
    }

    void updateAlarm(final Alarm alarm) {
        this.writeExecutor.execute(() -> this.alarmDao.update(alarm));
    }
}
