package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import java.util.LinkedHashSet;
import java.util.Set;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Next.next;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.saveToDatabase;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startDecreasingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.stopDecreasingVolume;

@Value
public class SetSleepTimerDecreasingVolumeEvent implements SleepTimerEvent {

    private final boolean decreasingVolume;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        final SleepTimer updatedSleepTimer = sleepTimer.setDecreasingVolume(decreasingVolume);

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
    }
}
