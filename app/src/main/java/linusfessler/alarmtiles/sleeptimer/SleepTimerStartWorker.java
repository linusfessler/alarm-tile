package linusfessler.alarmtiles.sleeptimer;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.inject.Inject;

import linusfessler.alarmtiles.App;

public class SleepTimerStartWorker extends Worker {

    @Inject
    SleepTimerRepository repository;

    @Inject
    SleepTimerWorkCoordinator workCoordinator;

    @Inject
    AudioManager audioManager;

    public SleepTimerStartWorker(final Context context, final WorkerParameters params) {
        super(context, params);
        ((App) this.getApplicationContext()).getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        final SleepTimer sleepTimer = this.repository.getSleepTimer().blockingFirst();

        final int originalVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sleepTimer.start(originalVolume);
        this.repository.updateSleepTimer(sleepTimer);

        this.workCoordinator.progress(sleepTimer);

        return Result.success();
    }
}
