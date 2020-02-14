package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startWith;

@Value
public class SetSleepTimerTimeUnitEvent implements SleepTimerEvent {

    private final TimeUnit timeUnit;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        final SleepTimer updatedSleepTimer = sleepTimer.setTimeUnit(timeUnit);

        final Set<SleepTimerEffect> effects = new LinkedHashSet<>();
        effects.add(saveToDatabase(updatedSleepTimer));

        if (updatedSleepTimer.isEnabled()) {
            effects.add(startWith(updatedSleepTimer));
            return dispatch(effects);
        } else {
            return next(updatedSleepTimer, effects);
        }
    }
}
