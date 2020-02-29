package linusfessler.alarmtiles.shared

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.UiModeManager
import android.content.ContentResolver
import android.database.ContentObserver
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Handler
import android.os.Vibrator
import android.provider.Settings
import android.text.format.DateFormat
import android.view.inputmethod.InputMethodManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import linusfessler.alarmtiles.shared.alarmconfig.AlarmConfigDatabase
import javax.inject.Named
import javax.inject.Singleton

@Module
class SharedModule(private val application: Application) {
    @Provides
    fun application(): Application {
        return application
    }

    @Provides
    @Singleton
    fun audioManager(): AudioManager {
        return application.getSystemService(AudioManager::class.java)
    }

    @Provides
    @Singleton
    fun alarmManager(): AlarmManager {
        return application.getSystemService(AlarmManager::class.java)
    }

    @Provides
    @Singleton
    fun notificationManager(): NotificationManager {
        return application.getSystemService(NotificationManager::class.java)
    }

    @Provides
    @Singleton
    fun cameraManager(): CameraManager {
        return application.getSystemService(CameraManager::class.java)
    }

    @Provides
    @Singleton
    fun inputMethodManager(): InputMethodManager {
        return application.getSystemService(InputMethodManager::class.java)
    }

    @Provides
    @Singleton
    fun contentResolver(): ContentResolver {
        return application.contentResolver
    }

    @Provides
    @Singleton
    fun vibrator(): Vibrator {
        return application.getSystemService(Vibrator::class.java)
    }

    @Provides
    @Singleton
    fun uiModeManager(): UiModeManager {
        return application.getSystemService(UiModeManager::class.java)
    }

    @Provides
    @Singleton
    fun alarmConfigDatabase(application: Application): AlarmConfigDatabase {
        return Room
                .databaseBuilder(application, AlarmConfigDatabase::class.java, "alarm-config-database")
                .build()
                .populate()
    }

    @Provides
    @Singleton
    fun volumeObservable(contentResolver: ContentResolver, mediaVolumeManager: MediaVolumeManager): Observable<Int> {
        return Observable.create { emitter: ObservableEmitter<Int> ->
            val contentObserver = object : ContentObserver(Handler()) {
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    val volume = mediaVolumeManager.volume
                    emitter.onNext(volume)
                }
            }

            contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, contentObserver)

            emitter.setCancellable {
                contentResolver.unregisterContentObserver(contentObserver)
            }
        }
    }

    @Provides
    @Named("is24Hours")
    @Singleton
    fun is24Hours(application: Application): Boolean {
        return DateFormat.is24HourFormat(application)
    }
}