package linusfessler.alarmtiles.sleeptimer;

import android.media.AudioManager;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SleepTimerWorkCoordinator {

    private final WorkManager workManager;
    private final AudioManager audioManager;

    @Inject
    SleepTimerWorkCoordinator(final WorkManager workManager, final AudioManager audioManager) {
        this.workManager = workManager;
        this.audioManager = audioManager;
    }

    void start() {
        this.requestWork(SleepTimerStartWorker.class, 0);
    }

    void progress(final SleepTimer sleepTimer) {
        final long millisElapsed = System.currentTimeMillis() - sleepTimer.getStartTimestamp();
        final long millisLeft = sleepTimer.getDuration() - millisElapsed;

        if (millisLeft <= 0) {
            this.stop();
            return;
        }

        final int volume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final long delay = millisLeft / volume;

        this.requestWork(SleepTimerProgressWorker.class, delay);
    }

    void stop() {
        this.workManager.cancelAllWorkByTag(SleepTimerStartWorker.class.getName());
        this.workManager.cancelAllWorkByTag(SleepTimerProgressWorker.class.getName());
        this.requestWork(SleepTimerStopWorker.class, 0);
    }

    private <T extends Worker> void requestWork(final Class<T> workerClass, final long delay) {
        final WorkRequest workRequest = new OneTimeWorkRequest.Builder(workerClass)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();

        this.workManager.enqueue(workRequest);
    }
}
