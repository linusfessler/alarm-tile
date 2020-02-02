package linusfessler.alarmtiles;

import android.media.AudioManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaVolumeManager {

    private final AudioManager audioManager;

    @Inject
    public MediaVolumeManager(final AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public int getVolume() {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void setVolume(int volume) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }
}
