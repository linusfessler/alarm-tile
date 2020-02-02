package linusfessler.alarmtiles;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaPlaybackManager {

    private final AudioManager audioManager;

    @Inject
    public MediaPlaybackManager(final AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void stopMediaPlayback() {
        this.requestAudioFocus();
    }

    @SuppressWarnings("squid:CallToDeprecatedMethod")
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
