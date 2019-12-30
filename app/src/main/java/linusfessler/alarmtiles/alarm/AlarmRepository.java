package linusfessler.alarmtiles.alarm;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.AppDatabase;
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
        this.writeExecutor = appDatabase.getWriteExecutor();
        this.alarmDao = appDatabase.alarmDao();

        this.alarm = this.alarmDao.select();
    }

    void update(final Alarm alarm) {
        this.writeExecutor.execute(() -> this.alarmDao.update(alarm));
    }
}
