package linusfessler.alarmtiles;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class VolumeObserver implements LifecycleObserver {

    private final AudioManager audioManager;
    private final ContentResolver contentResolver;
    private final BehaviorSubject<Integer> volumeSubject;
    private final ContentObserver contentObserver;

    @Inject
    public VolumeObserver(final AudioManager audioManager, final ContentResolver contentResolver, final Lifecycle lifecycle) {
        this.audioManager = audioManager;
        this.contentResolver = contentResolver;
        this.volumeSubject = BehaviorSubject.createDefault(this.getVolume());
        this.contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(final boolean selfChange) {
                super.onChange(selfChange);
                final int volume = VolumeObserver.this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                VolumeObserver.this.volumeSubject.onNext(volume);
            }
        };

        lifecycle.addObserver(this);
    }

    public Observable<Integer> getObservable() {
        return this.volumeSubject;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        this.contentResolver.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, this.contentObserver);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        this.contentResolver.unregisterContentObserver(this.contentObserver);
    }

    private int getVolume() {
        return this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
