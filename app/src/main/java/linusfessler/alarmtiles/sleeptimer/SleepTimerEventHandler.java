package linusfessler.alarmtiles.sleeptimer;

import androidx.annotation.NonNull;

import com.spotify.mobius.Next;
import com.spotify.mobius.Update;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;
import static com.spotify.mobius.Next.noChange;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.cancel;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.finishWith;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.hideNotification;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.loadFromDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.resetVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.scheduleFinish;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.setVolumeToZero;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.showNotification;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.start;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startDecreasingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startWith;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.stopDecreasingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.stopMediaPlayback;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.unscheduleFinish;

@Singleton
public class SleepTimerEventHandler implements Update<SleepTimer, SleepTimerEvent, SleepTimerEffect> {

    @Inject
    public SleepTimerEventHandler() {

    }

    @NonNull
    @Override
    public Next<SleepTimer, SleepTimerEffect> update(@NonNull final SleepTimer sleepTimer, final SleepTimerEvent event) {
        return event.map(
                load -> dispatch(effects(loadFromDatabase())),

                loaded -> next(loaded.sleepTimer(), loaded.sleepTimer().isEnabled()
                        ? effects(showNotification())
                        : effects()),

                toggle -> dispatch(effects(sleepTimer.isEnabled() ? cancel() : start())),

                start -> dispatch(effects(startWith(sleepTimer))),

                startWith -> {
                    final SleepTimer startedSleepTimer = startWith.sleepTimer().start();
                    return next(startedSleepTimer, effects(
                            saveToDatabase(startedSleepTimer),
                            showNotification(),
                            startDecreasingVolume(startedSleepTimer.getMillisLeft()),
                            scheduleFinish(startedSleepTimer.getMillisLeft())));
                },

                volumeChanged -> sleepTimer.isEnabled() && sleepTimer.isDecreasingVolume()
                        ? dispatch(effects(startDecreasingVolume(sleepTimer.getMillisLeft())))
                        : noChange(),

                setTime -> {
                    final SleepTimer updatedSleepTimer = sleepTimer.setTime(setTime.time());

                    final Set<SleepTimerEffect> effects = new LinkedHashSet<>();
                    effects.add(saveToDatabase(updatedSleepTimer));

                    if (updatedSleepTimer.isEnabled()) {
                        effects.add(startWith(updatedSleepTimer));
                        return dispatch(effects);
                    } else {
                        return next(updatedSleepTimer, effects);
                    }
                },

                setTimeUnit -> {
                    final SleepTimer updatedSleepTimer = sleepTimer.setTimeUnit(setTimeUnit.timeUnit());

                    final Set<SleepTimerEffect> effects = new LinkedHashSet<>();
                    effects.add(saveToDatabase(updatedSleepTimer));

                    if (updatedSleepTimer.isEnabled()) {
                        effects.add(startWith(updatedSleepTimer));
                        return dispatch(effects);
                    } else {
                        return next(updatedSleepTimer, effects);
                    }
                },

                setDecreasingVolume -> {
                    final SleepTimer updatedSleepTimer = sleepTimer.setDecreasingVolume(setDecreasingVolume.decreasingVolume());

                    final Set<SleepTimerEffect> effects = new LinkedHashSet<>();
                    effects.add(saveToDatabase(updatedSleepTimer));

                    if (updatedSleepTimer.isEnabled()) {
                        if (updatedSleepTimer.isDecreasingVolume()) {
                            effects.add(startDecreasingVolume(updatedSleepTimer.getMillisLeft()));
                        } else {
                            effects.add(stopDecreasingVolume());
                        }
                    }

                    return next(updatedSleepTimer, effects);
                },

                setResettingVolume -> {
                    final SleepTimer updatedSleepTimer = sleepTimer.setResettingVolume(setResettingVolume.resettingVolume());
                    return next(updatedSleepTimer, effects(saveToDatabase(updatedSleepTimer)));
                },

                cancel -> {
                    final SleepTimer stoppedSleepTimer = sleepTimer.stop();
                    final Set<SleepTimerEffect> effects = new LinkedHashSet<>();

                    effects.add(saveToDatabase(stoppedSleepTimer));
                    effects.add(hideNotification());
                    effects.add(stopDecreasingVolume());
                    effects.add(unscheduleFinish());
                    if (stoppedSleepTimer.shouldResetVolume()) {
                        effects.add(resetVolume(sleepTimer.getOriginalVolume()));
                    }

                    return next(stoppedSleepTimer, effects);
                },

                finish -> dispatch(effects(finishWith(sleepTimer))),

                finishWith -> {
                    final SleepTimer stoppedSleepTimer = finishWith.sleepTimer().stop();
                    final Set<SleepTimerEffect> effects = new LinkedHashSet<>();

                    effects.add(saveToDatabase(stoppedSleepTimer));
                    effects.add(hideNotification());
                    effects.add(stopDecreasingVolume());
                    effects.add(unscheduleFinish());
                    effects.add(stopMediaPlayback());
                    if (stoppedSleepTimer.shouldResetVolume()) {
                        effects.add(resetVolume(finishWith.sleepTimer().getOriginalVolume()));
                    } else if (stoppedSleepTimer.isDecreasingVolume()) {
                        effects.add(setVolumeToZero());
                    }

                    return next(stoppedSleepTimer, effects);
                }
        );
    }
}
