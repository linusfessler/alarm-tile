package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import java.util.Set;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.hideNotification;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.setVolumeToZero;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.stopDecreasingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.stopMediaPlayback;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.unscheduleFinish;

@Value
public class FinishWithSleepTimerEvent implements SleepTimerEvent {

    private final SleepTimer sleepTimerToFinish;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        final SleepTimer stoppedSleepTimer = sleepTimer.stop();

        final Set<SleepTimerEffect> effects = effects(
                saveToDatabase(stoppedSleepTimer),
                hideNotification(),
                stopDecreasingVolume(),
                unscheduleFinish(),
                stopMediaPlayback()
        );
        if (stoppedSleepTimer.isDecreasingVolume()) {
            effects.add(setVolumeToZero());
        }

        return next(stoppedSleepTimer, effects);
    }
}
