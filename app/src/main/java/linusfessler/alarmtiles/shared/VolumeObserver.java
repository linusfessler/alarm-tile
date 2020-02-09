package linusfessler.alarmtiles.shared;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class VolumeObserver {

    private final Observable<Integer> volumeSubject;

    @Inject
    public VolumeObserver(final MediaVolumeManager mediaVolumeManager, final ContentResolver contentResolver) {
        volumeSubject = BehaviorSubject
                .createDefault(mediaVolumeManager.getVolume())
                .concatWith(Observable.create(emitter -> {
                    final ContentObserver contentObserver = new ContentObserver(new Handler()) {
                        @Override
                        public void onChange(final boolean selfChange) {
                            super.onChange(selfChange);
                            final int volume = mediaVolumeManager.getVolume();
                            emitter.onNext(volume);
                        }
                    };

                    contentResolver.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, contentObserver);

                    emitter.setCancellable(() -> contentResolver.unregisterContentObserver(contentObserver));
                }));
    }

    public Observable<Integer> getObservable() {
        return volumeSubject.hide();
    }
}
