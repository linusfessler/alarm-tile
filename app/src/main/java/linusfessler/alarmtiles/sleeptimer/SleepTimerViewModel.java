package linusfessler.alarmtiles.sleeptimer;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import linusfessler.alarmtiles.TimeFormatter;

public class SleepTimerViewModel extends ViewModel {

    //    private final WorkManager workManager;
//    private final AudioManager audioManager;
    private final SleepTimerRepository repository;
    private final String tileLabel;
    private final TimeFormatter timeFormatter;

    //    private final int maxVolume;
    private final Observable<SleepTimer> sleepTimerObservable;
    private final Observable<String> tileLabelObservable;

    public SleepTimerViewModel(final SleepTimerRepository repository, final String tileLabel, final TimeFormatter timeFormatter) {
//        this.workManager = WorkManager.getInstance(application);
//        this.audioManager = application.getSystemService(AudioManager.class);
        this.repository = repository;
        this.tileLabel = tileLabel;
        this.timeFormatter = timeFormatter;

//        this.maxVolume = this.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        this.sleepTimerObservable = this.repository.getSleepTimer();

        this.tileLabelObservable = this.sleepTimerObservable.switchMap(sleepTimer -> {
            if (sleepTimer == null || !sleepTimer.isEnabled()) {
                return Observable.just(this.tileLabel);
            }

            final long millisElapsed = System.currentTimeMillis() - sleepTimer.getStartTimeStamp();
            final long millisLeft = sleepTimer.getDuration() - millisElapsed;
            final long secondsLeft = Math.round(millisLeft / 1000.);

            return Observable.intervalRange(0, secondsLeft, 0, 1, TimeUnit.SECONDS)
                    .map(zeroBasedSecondsPassed -> {
                        final String formattedTimeLeft = this.timeFormatter.format(1000 * (secondsLeft - zeroBasedSecondsPassed), TimeUnit.SECONDS);
                        return this.tileLabel + "\n" + formattedTimeLeft;
                    });
        });
    }

    public Observable<SleepTimer> getSleepTimer() {
        return this.sleepTimerObservable;
    }

    public Observable<String> getTileLabel() {
        return this.tileLabelObservable;
    }

    public void toggle(final SleepTimer sleepTimer) {
        if (sleepTimer.isEnabled()) {
            sleepTimer.disable();

//            this.workManager.cancelWorkById(sleepTimer.getWorkRequestId());
        } else {
            sleepTimer.enable();

//            final int originalVolume = this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            this.requestAudioFocus();
//            this.requestFadeOrStop(sleepTimer);
        }
        this.repository.updateSleepTimer(sleepTimer);
    }

    private void requestFadeOrStop(final SleepTimer sleepTimer) {
        final long millisElapsed = System.currentTimeMillis() - sleepTimer.getStartTimeStamp();
        final long millisLeft = sleepTimer.getDuration() - millisElapsed;

        // TODO: Je nach aktueller Lautstärke Worker class wählen, wenn Lautstärke == 1: Stop Worker
        // Der Fade Worker macht einfach leiser (immer nur 1er Schritte)
        // Der Stop Worker requests audio focus und schaltet den sleepTimer aus

//        this.latestWorkRequest = new OneTimeWorkRequest.Builder(StopPlaybackWorker.class)
//                .setInitialDelay(millisLeft, TimeUnit.MILLISECONDS)
//                .build();
//        this.workManager.enqueue(this.latestWorkRequest);
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            final AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .build();

//            this.audioManager.requestAudioFocus(audioFocusRequest);
        } else {
//            this.audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }
}
