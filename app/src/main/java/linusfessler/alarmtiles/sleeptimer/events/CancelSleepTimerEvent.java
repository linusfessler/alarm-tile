package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.hideNotification;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.stopDecreasingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.unscheduleFinish;

@Value
public class CancelSleepTimerEvent implements SleepTimerEvent {

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        final SleepTimer stoppedSleepTimer = sleepTimer.stop();
        return next(stoppedSleepTimer, effects(
                saveToDatabase(stoppedSleepTimer),
                hideNotification(),
                stopDecreasingVolume(),
                unscheduleFinish()
        ));
    }
}
