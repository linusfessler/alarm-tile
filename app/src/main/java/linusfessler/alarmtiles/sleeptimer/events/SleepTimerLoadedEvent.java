package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.showNotification;

@Value
public class SleepTimerLoadedEvent implements SleepTimerEvent {

    private final SleepTimer loadedSleepTimer;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        return next(loadedSleepTimer, loadedSleepTimer.isEnabled()
                ? effects(showNotification())
                : effects());
    }
}
