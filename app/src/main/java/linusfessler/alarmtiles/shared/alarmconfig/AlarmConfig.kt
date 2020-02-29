package linusfessler.alarmtiles.shared.alarmconfig

import android.media.RingtoneManager
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity
data class AlarmConfig(
        @field:PrimaryKey val id: Long = 0,
        val alarmSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
        val vibrate: Boolean = false,
        val flashlight: Boolean = false,
        val snoozeTime: Double = 10.0,
        val snoozeTimeUnit: TimeUnit = TimeUnit.MINUTES
)