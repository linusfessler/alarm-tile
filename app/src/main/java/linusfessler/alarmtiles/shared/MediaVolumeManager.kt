package linusfessler.alarmtiles.shared

import android.media.AudioManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaVolumeManager @Inject constructor(private val audioManager: AudioManager) {
    var volume: Int
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        set(volume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
        }
}