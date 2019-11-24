package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import lombok.Getter;

public class SleepTimerViewModel extends AndroidViewModel {

    private final SleepTimerRepository repository;

    @Getter
    private final LiveData<SleepTimer> sleepTimer;

    public SleepTimerViewModel(@NonNull final Application application) {
        super(application);
        this.repository = new SleepTimerRepository(application);
        this.sleepTimer = this.repository.getSleepTimer();
    }

    public void setSleepTimer(final SleepTimer sleepTimer) {
        this.repository.setSleepTimer(sleepTimer);
    }
}
