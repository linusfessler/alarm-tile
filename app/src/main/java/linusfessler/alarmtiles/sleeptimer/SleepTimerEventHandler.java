package linusfessler.alarmtiles.sleeptimer;

import androidx.annotation.NonNull;

import com.spotify.mobius.Next;
import com.spotify.mobius.Update;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.sleeptimer.events.SleepTimerEvent;

@Singleton
public class SleepTimerEventHandler implements Update<SleepTimer, SleepTimerEvent, SleepTimerEffect> {

    @Inject
    public SleepTimerEventHandler() {
    }

    @NonNull
    @Override
    public Next<SleepTimer, SleepTimerEffect> update(@NonNull final SleepTimer sleepTimer, final SleepTimerEvent event) {
        return event.update(sleepTimer);
    }
}
