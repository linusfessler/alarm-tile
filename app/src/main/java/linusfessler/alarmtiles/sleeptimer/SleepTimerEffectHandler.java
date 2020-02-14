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
import linusfessler.alarmtiles.sleeptimer.events.CancelSleepTimerEvent;
import linusfessler.alarmtiles.sleeptimer.events.FinishSleepTimerEvent;
import linusfessler.alarmtiles.sleeptimer.events.FinishWithSleepTimerEvent;
import linusfessler.alarmtiles.sleeptimer.events.SleepTimerEvent;
import linusfessler.alarmtiles.sleeptimer.events.SleepTimerLoadedEvent;
import linusfessler.alarmtiles.sleeptimer.events.StartSleepTimerEvent;
import linusfessler.alarmtiles.sleeptimer.events.StartWithSleepTimerEvent;

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
                                                ? new FinishWithSleepTimerEvent(sleepTimer)
                                                : new SleepTimerLoadedEvent(sleepTimer)))),

                        saveToDatabase -> repository.update(saveToDatabase.sleepTimer()),

                        start -> eventConsumer.accept(new StartSleepTimerEvent()),

                        cancel -> eventConsumer.accept(new CancelSleepTimerEvent()),

                        startWith -> {
                            final SleepTimer preparedSleepTimer = startWith.sleepTimer().prepareForStart(System.currentTimeMillis());
                            eventConsumer.accept(new StartWithSleepTimerEvent(preparedSleepTimer));
                        },

                        finishWith -> eventConsumer.accept(new FinishWithSleepTimerEvent(finishWith.sleepTimer())),

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
                                    .subscribe(zero -> eventConsumer.accept(new FinishSleepTimerEvent())));
                        },

                        unscheduleFinish -> finishDisposable.clear(),

                        setVolumeToZero -> mediaVolumeManager.setVolume(0),

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
