package linusfessler.alarmtiles.shared

import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Handler
import androidx.lifecycle.LifecycleService
import linusfessler.alarmtiles.FlashlightManager
import linusfessler.alarmtiles.shared.alarm.config.AlarmConfigViewModel
import javax.inject.Inject

class AlarmService : LifecycleService() {
    @Inject
    lateinit var viewModel: AlarmConfigViewModel

    @Inject
    lateinit var vibrationManager: VibrationManager

    @Inject
    lateinit var flashlightManager: FlashlightManager

    private var alarmSound: Ringtone? = null
//    private var alarmIsActive = false

    override fun onCreate() {
        super.onCreate()

        (application as App)
                .sharedComponent
                .inject(this)

//        alarmIsActive = true

        alarmSound = RingtoneManager.getRingtone(this, viewModel.config.alarmSoundUri)
        alarmSound!!.audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
        alarmSound!!.play()

        if (viewModel.config.vibrate) {
//            val vibrationPause: Long = preferences.getString(getString(R.string.pref_pause_duration_key), String.valueOf(DEFAULT_VIBRATION_PATTERN.get(0))).toLong()
//            val vibrationDuration: Long = preferences.getString(getString(R.string.pref_vibration_duration_key), String.valueOf(DEFAULT_VIBRATION_PATTERN.get(1))).toLong()
//            val vibrationPattern = longArrayOf(vibrationPause, vibrationDuration)
            vibrationManager.vibrate()
        }

        if (viewModel.config.flashlight) {
            flashlightManager.turnOn()
        }


        Handler().postDelayed(Runnable { stopSelf() }, 2000)

//        val increaseVolume: Boolean = preferences.getBoolean(getString(R.string.pref_increase_volume_key), false)
//        if (increaseVolume) {
//            Executors.newFixedThreadPool(1).execute {
//                val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
//                if (audioManager != null) {
//                    val originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
//                    val duration: Int = 1000 * preferences.getInt(getString(R.string.pref_increase_volume_duration_key), 0)
//                    val endTime = duration + System.currentTimeMillis()
//                    var currentVolume = 0
//                    while (alarmIsActive && currentVolume < originalVolume) {
//                        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, ++currentVolume, 0)
//                        val timeLeft = endTime - System.currentTimeMillis()
//                        val timeStep = Math.max(timeLeft, 0) / Math.max(originalVolume - currentVolume, 1)
//                        try {
//                            Thread.sleep(timeStep)
//                        } catch (e: InterruptedException) {
//                            e.printStackTrace()
//                        }
//                    }
//                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0)
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        alarmIsActive = false

        alarmSound!!.stop()

        if (viewModel.config.vibrate) {
            vibrationManager.cancel()
        }

        if (viewModel.config.flashlight) {
            flashlightManager.turnOn()
        }
    }
}