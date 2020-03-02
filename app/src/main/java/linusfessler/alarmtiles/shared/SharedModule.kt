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
import linusfessler.alarmtiles.shared.alarm.AlarmDatabase
import javax.inject.Named
import javax.inject.Singleton

@Module
class SharedModule(private val application: Application) {
    @Provides
    fun application(): Application = application

    @Provides
    @Singleton
    fun audioManager(): AudioManager = application.getSystemService(AudioManager::class.java)

    @Provides
    @Singleton
    fun alarmManager(): AlarmManager = application.getSystemService(AlarmManager::class.java)

    @Provides
    @Singleton
    fun notificationManager(): NotificationManager = application.getSystemService(NotificationManager::class.java)

    @Provides
    @Singleton
    fun cameraManager(): CameraManager = application.getSystemService(CameraManager::class.java)

    @Provides
    @Singleton
    fun inputMethodManager(): InputMethodManager = application.getSystemService(InputMethodManager::class.java)

    @Provides
    @Singleton
    fun contentResolver(): ContentResolver = application.contentResolver

    @Provides
    @Singleton
    fun vibrator(): Vibrator = application.getSystemService(Vibrator::class.java)

    @Provides
    @Singleton
    fun uiModeManager(): UiModeManager = application.getSystemService(UiModeManager::class.java)

    @Provides
    @Singleton
    fun alarmDatabase(application: Application): AlarmDatabase = Room
            .databaseBuilder(application, AlarmDatabase::class.java, "alarm-database")
            .build()
            .populate()

    @Provides
    @Singleton
    fun volumeObservable(contentResolver: ContentResolver, mediaVolumeManager: MediaVolumeManager): Observable<Int> =
            Observable.create { emitter: ObservableEmitter<Int> ->
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

    @Provides
    @Named("is24Hours")
    @Singleton
    fun is24Hours(application: Application): Boolean = DateFormat.is24HourFormat(application)
}