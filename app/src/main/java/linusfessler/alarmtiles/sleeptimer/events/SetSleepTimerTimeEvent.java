package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import java.util.LinkedHashSet;
import java.util.Set;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startWith;

@Value
public class SetSleepTimerTimeEvent implements SleepTimerEvent {

    private final double time;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        final SleepTimer updatedSleepTimer = sleepTimer.setTime(time);

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
