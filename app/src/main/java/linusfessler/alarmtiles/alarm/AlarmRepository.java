package linusfessler.alarmtiles.alarm;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.core.AppDatabase;
import lombok.AccessLevel;
import lombok.Getter;

@Singleton
class AlarmRepository {

    private final ExecutorService writeExecutor;
    private final AlarmDao alarmDao;

    @Getter(AccessLevel.PACKAGE)
    private final LiveData<Alarm> alarm;

    @Inject
    AlarmRepository(final AppDatabase appDatabase) {
        writeExecutor = appDatabase.getWriteExecutor();
        alarmDao = appDatabase.alarmDao();

        alarm = alarmDao.select();
    }

    void update(final Alarm alarm) {
        writeExecutor.execute(() -> alarmDao.update(alarm));
    }
}
