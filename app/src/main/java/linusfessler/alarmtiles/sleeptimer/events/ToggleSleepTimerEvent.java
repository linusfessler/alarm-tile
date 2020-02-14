package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.cancel;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.start;

@Value
public class ToggleSleepTimerEvent implements SleepTimerEvent {

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        return dispatch(effects(sleepTimer.isEnabled() ? cancel() : start()));
    }
}
