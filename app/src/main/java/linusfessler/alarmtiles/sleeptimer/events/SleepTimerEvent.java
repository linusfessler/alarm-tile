package linusfessler.alarmtiles.sleeptimer.events;

import com.spotify.mobius.Next;

import linusfessler.alarmtiles.sleeptimer.SleepTimer;
import linusfessler.alarmtiles.sleeptimer.SleepTimerEffect;

public interface SleepTimerEvent {

    Next<SleepTimer, SleepTimerEffect> update(SleepTimer sleepTimer);
}
