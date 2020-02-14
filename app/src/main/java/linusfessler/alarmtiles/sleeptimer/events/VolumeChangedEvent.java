package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.noChange;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.startDecreasingVolume;

@Value
public class VolumeChangedEvent implements SleepTimerEvent {

    private final int volume;

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        return sleepTimer.isEnabled() && sleepTimer.isDecreasingVolume()
                ? dispatch(effects(startDecreasingVolume(sleepTimer.getMillisLeft())))
                : noChange();
    }
}
