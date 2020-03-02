package linusfessler.alarmtiles.shared

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibrationManager @Inject constructor(private val vibrator: Vibrator) {
    fun vibrate(milliseconds: Long = DEFAULT_DURATION, amplitude: Int = DEFAULT_AMPLITUDE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, amplitude))
        } else {
            @Suppress("deprecation")
            vibrator.vibrate(milliseconds)
        }
    }

    fun vibrate(pattern: LongArray = DEFAULT_VIBRATION_PATTERN) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
        } else {
            @Suppress("deprecation")
            vibrator.vibrate(pattern, 0);
        }
    }

    fun cancel() {
        vibrator.cancel()
    }

    companion object {
        private const val DEFAULT_DURATION = 250L
        private const val DEFAULT_AMPLITUDE = -1
        private val DEFAULT_VIBRATION_PATTERN = longArrayOf(500, 500)
    }
}