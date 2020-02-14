package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;
import lombok.Value;

import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEffect.finishWith;

@Value
public class FinishSleepTimerEvent implements SleepTimerEvent {

    @Override
    public Next<SleepTimer, SleepTimerEffect> update(final SleepTimer sleepTimer) {
        return dispatch(effects(finishWith(sleepTimer)));
    }
}
