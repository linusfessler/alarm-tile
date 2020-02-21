package linusfessler.alarmtiles.shared

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibrationManager @Inject constructor(private val vibrator: Vibrator) {
    fun vibrate(milliseconds: Long = DEFAULT_DURATION.toLong(), amplitude: Int = DEFAULT_AMPLITUDE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(milliseconds)
        }
    }

    companion object {
        private const val DEFAULT_DURATION = 250
        private const val DEFAULT_AMPLITUDE = -1
    }
}