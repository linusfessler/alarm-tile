package linusfessler.alarmtiles.sleeptimer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.inject.Inject;

import linusfessler.alarmtiles.App;

public class SleepTimerStopWorker extends Worker {

    @Inject
    SleepTimerRepository repository;

    @Inject
    AudioManager audioManager;

    public SleepTimerStopWorker(final Context context, final WorkerParameters params) {
        super(context, params);
        ((App) this.getApplicationContext()).getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        final SleepTimer sleepTimer = this.repository.getSleepTimer().blockingFirst();

        this.stopAnyPlayback();
        this.setVolume(sleepTimer.getOriginalVolume());

        sleepTimer.stop();
        this.repository.updateSleepTimer(sleepTimer);

        return Result.success();
    }

    private void stopAnyPlayback() {
        this.requestAudioFocus();
    }

    private void setVolume(final int volume) {
        this.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
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
