package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Should only be used on background threads
 */

@Singleton
class SleepTimerWorker {

    private final Application application;
    private final SleepTimerRepository repository;
    private final AudioManager audioManager;

    private final SleepTimer sleepTimer;
    private final Handler handler = new Handler();
    private final Runnable fadeRunnable = this::fade;
    private final Runnable finishRunnable = this::finish;

    @Inject
    SleepTimerWorker(final Application application, final SleepTimerRepository repository, final AudioManager audioManager) {
        this.application = application;
        this.repository = repository;
        this.audioManager = audioManager;

        this.sleepTimer = repository.getSleepTimer().blockingFirst();
    }

    void start() {
        this.startSleepTimer();
        this.scheduleFade();
        this.scheduleFinish();
    }

    void onVolumeChanged() {
        this.rescheduleFade();
    }

    void cancel() {
        this.cancelScheduledFade();
        this.cancelScheduledFinish();
        this.resetSleepTimer();
    }

    void add1Minute() {
        this.addMinutes(1);
    }

    void add15Minutes() {
        this.addMinutes(15);
    }

    private void startSleepTimer() {
        final int originalVolume = this.getVolume();
        this.sleepTimer.start(originalVolume);
        this.repository.updateSleepTimer(this.sleepTimer);
    }

    private void resetSleepTimer() {
        this.setVolume(this.sleepTimer.getOriginalVolume());
        this.sleepTimer.reset();
        this.repository.updateSleepTimer(this.sleepTimer);
    }

    private void addMinutes(final int minutes) {
        this.sleepTimer.addAdditionalTime(minutes * 60 * 1000L);
        this.repository.updateSleepTimer(this.sleepTimer);

        this.rescheduleFade();
        this.rescheduleFinish();
    }

    private void scheduleFade() {
        final int volume = this.getVolume();
        if (this.sleepTimer.getConfig().isFading() && volume > 0) {
            final long delayMillis = this.sleepTimer.getMillisLeft() / volume;
            this.handler.postDelayed(this.fadeRunnable, delayMillis);
        }
    }

    private void scheduleFinish() {
        final long delayMillis = this.sleepTimer.getMillisLeft();
        this.handler.postDelayed(this.finishRunnable, delayMillis);
    }

    private void rescheduleFade() {
        if (this.sleepTimer.getConfig().isFading()) {
            this.cancelScheduledFade();
            this.scheduleFade();
        }
    }

    private void rescheduleFinish() {
        this.cancelScheduledFinish();
        this.scheduleFinish();
    }

    private void cancelScheduledFade() {
        this.handler.removeCallbacks(this.fadeRunnable);
    }

    private void cancelScheduledFinish() {
        this.handler.removeCallbacks(this.finishRunnable);
    }

    private void fade() {
        final int volume = this.getVolume();
        this.setVolume(volume - 1);
        this.scheduleFade();
    }

    private void finish() {
        this.cancelScheduledFade();
        this.stopMediaPlayback();
        this.resetSleepTimer();
        SleepTimerService.finish(this.application);
    }

    private void stopMediaPlayback() {
        this.requestAudioFocus();
    }

    private int getVolume() {
        return this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void setVolume(final int volume) {
        if (volume >= 0) {
            this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            final AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .build();

            this.audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            this.audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }
}
