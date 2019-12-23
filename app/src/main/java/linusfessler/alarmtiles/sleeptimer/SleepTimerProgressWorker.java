package linusfessler.alarmtiles.sleeptimer;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.inject.Inject;

import linusfessler.alarmtiles.App;

public class SleepTimerProgressWorker extends Worker {

    @Inject
    SleepTimerRepository repository;

    @Inject
    SleepTimerWorkCoordinator workCoordinator;

    @Inject
    AudioManager audioManager;

    public SleepTimerProgressWorker(final Context context, final WorkerParameters params) {
        super(context, params);
        ((App) this.getApplicationContext()).getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        final SleepTimer sleepTimer = this.repository.getSleepTimer().blockingFirst();

        if (sleepTimer.isFading()) {
            this.turnVolumeDown(1);
        }

        this.workCoordinator.progress(sleepTimer);

        return Result.success();
    }

    private void turnVolumeDown(final int amount) {
        final int volume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume - amount, AudioManager.FLAG_SHOW_UI);
    }
}
