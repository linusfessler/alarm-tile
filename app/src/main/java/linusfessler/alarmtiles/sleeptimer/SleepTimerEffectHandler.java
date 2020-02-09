package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;

import androidx.annotation.NonNull;

import com.spotify.mobius.Connectable;
import com.spotify.mobius.Connection;
import com.spotify.mobius.functions.Consumer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.shared.MediaPlaybackManager;
import linusfessler.alarmtiles.shared.MediaVolumeManager;

import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.cancel;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.finish;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.finishWith;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.loaded;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.start;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.startWith;

@Singleton
public class SleepTimerEffectHandler implements Connectable<SleepTimerEffect, SleepTimerEvent> {

    private final Application application;
    private final SleepTimerRepository repository;
    private final MediaVolumeManager mediaVolumeManager;
    private final MediaPlaybackManager mediaPlaybackManager;

    @Inject

    public SleepTimerEffectHandler(final Application application, final SleepTimerRepository repository, final MediaVolumeManager mediaVolumeManager, final MediaPlaybackManager mediaPlaybackManager) {
        this.application = application;
        this.repository = repository;
        this.mediaVolumeManager = mediaVolumeManager;
        this.mediaPlaybackManager = mediaPlaybackManager;
    }

    @Override
    @NonNull
    public Connection<SleepTimerEffect> connect(@NonNull final Consumer<SleepTimerEvent> eventConsumer) {
        return new Connection<SleepTimerEffect>() {

            private final CompositeDisposable disposable = new CompositeDisposable();
            private final CompositeDisposable volumeDisposable = new CompositeDisposable();
            private final CompositeDisposable finishDisposable = new CompositeDisposable();

            @Override
            public void accept(@NonNull final SleepTimerEffect effect) {
                effect.match(
                        loadFromDatabase -> disposable.add(repository.getSleepTimer()
                                .take(1)
                                .subscribe(sleepTimer ->
                                        eventConsumer.accept(sleepTimer.isEnabled() && sleepTimer.getMillisLeft() <= 0
                                                ? finishWith(sleepTimer)
                                                : loaded(sleepTimer)))),

                        saveToDatabase -> repository.update(saveToDatabase.sleepTimer()),

                        start -> eventConsumer.accept(start()),

                        cancel -> eventConsumer.accept(cancel()),

                        startWith -> {
                            final SleepTimer preparedSleepTimer = startWith.sleepTimer().prepareForStart(System.currentTimeMillis(), mediaVolumeManager.getVolume());
                            eventConsumer.accept(startWith(preparedSleepTimer));
                        },

                        finishWith -> eventConsumer.accept(finishWith(finishWith.sleepTimer())),

                        startDecreasingVolume -> {
                            final int volume = mediaVolumeManager.getVolume();
                            final long millisLeft = startDecreasingVolume.millisLeft();
                            final long millisPerStep = millisLeft / volume;

                            volumeDisposable.clear();
                            volumeDisposable.add(Observable
                                    .intervalRange(1, volume, millisPerStep, millisPerStep, TimeUnit.MILLISECONDS)
                                    .map(inverseVolume -> volume - inverseVolume.intValue())
                                    .subscribe(mediaVolumeManager::setVolume));
                        },

                        stopDecreasingVolume -> volumeDisposable.clear(),

                        scheduleFinish -> {
                            finishDisposable.clear();
                            finishDisposable.add(Observable
                                    .timer(scheduleFinish.millisLeft(), TimeUnit.MILLISECONDS)
                                    .subscribe(zero -> eventConsumer.accept(finish())));
                        },

                        unscheduleFinish -> finishDisposable.clear(),

                        setVolumeToZero -> mediaVolumeManager.setVolume(0),

                        resetVolume -> mediaVolumeManager.setVolume(resetVolume.originalVolume()),

                        stopMediaPlay -> mediaPlaybackManager.stopMediaPlayback(),

                        showNotification -> SleepTimerNotificationService.start(application),

                        hideNotification -> SleepTimerNotificationService.stop(application)
                );
            }

            @Override
            public void dispose() {
                disposable.dispose();
            }
        };
    }
}
