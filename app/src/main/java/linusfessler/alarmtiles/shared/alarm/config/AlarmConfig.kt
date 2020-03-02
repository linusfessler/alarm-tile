package linusfessler.alarmtiles.shared.alarm.config

import android.media.RingtoneManager
import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import linusfessler.alarmtiles.shared.data.Time
import java.util.concurrent.TimeUnit

@Entity
data class AlarmConfig(
        @field:PrimaryKey val id: Long = 0,
        val alarmSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
        val vibrate: Boolean = false,
        val flashlight: Boolean = false,
        @Embedded val snoozeDuration: Time = Time(10.0, TimeUnit.MINUTES)
)