package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.scheduleFinish;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.showNotification;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startDecreasingVolume;

@Value
public class StartWithSleepTimerEvent implements SleepTimerEvent {

    private final SleepTimer sleepTimerToStart;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        final SleepTimer startedSleepTimer = sleepTimerToStart.start();
        return next(startedSleepTimer, effects(
                saveToDatabase(startedSleepTimer),
                showNotification(),
                startDecreasingVolume(startedSleepTimer.getMillisLeft()),
                scheduleFinish(startedSleepTimer.getMillisLeft())
        ));
    }
}
