package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.VolumeObserver;

class SleepTimerWorker implements LifecycleObserver {

    private final Application application;
    private final SleepTimerRepository repository;
    private final AudioManager audioManager;
    private final VolumeObserver volumeObserver;

    // TODO: Convert handler into RxJava
    // TODO: Create observer class that performs actions like changing volume / finish sleep timer
    private final Handler handler = new Handler();
    private final Runnable fadeRunnable = this::fade;
    private final Runnable finishRunnable = this::finish;

    private SleepTimer sleepTimer;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    SleepTimerWorker(final Application application, final SleepTimerRepository repository, final AudioManager audioManager, final VolumeObserver volumeObserver, final Lifecycle lifecycle) {
        this.application = application;
        this.repository = repository;
        this.audioManager = audioManager;
        this.volumeObserver = volumeObserver;

        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        // Sleep timer observable that is guaranteed to start with an enabled sleep timer
        final Observable<SleepTimer> sleepTimerObservable = this.repository.getSleepTimer()
                .skipWhile(newSleepTimer -> !newSleepTimer.isEnabled());

        this.disposable.add(sleepTimerObservable.subscribe(newSleepTimer -> {
            this.sleepTimer = newSleepTimer;

            if (this.sleepTimer.isEnabled()) {
                this.rescheduleFade();
                this.rescheduleFinish();
            }
        }));

        this.disposable.add(this.volumeObserver.getObservable()
                .skipUntil(sleepTimerObservable)
                .subscribe(volume -> this.rescheduleFade()));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.cancelScheduledFade();
        this.cancelScheduledFinish();
        this.disposable.dispose();
    }

    private void rescheduleFade() {
        this.cancelScheduledFade();
        this.scheduleFade();
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

    private void scheduleFade() {
        final int volume = this.getVolume();
        if (volume > 0) {
            final long delayMillis = this.sleepTimer.getMillisLeft() / volume;
            this.handler.postDelayed(this.fadeRunnable, delayMillis);
        }
    }

    private void scheduleFinish() {
        final long millisLeft = this.sleepTimer.getMillisLeft();
        this.handler.postDelayed(this.finishRunnable, millisLeft);
    }

    private void fade() {
        final int volume = this.getVolume();
        this.setVolume(volume - 1);
        this.rescheduleFade();
    }

    private void finish() {
        this.cancelScheduledFade();
        this.stopMediaPlayback();

        this.sleepTimer.finish();
        this.repository.update(this.sleepTimer);

        SleepTimerService.stop(this.application);
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
