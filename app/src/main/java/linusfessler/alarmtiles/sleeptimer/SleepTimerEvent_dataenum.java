package linusfessler.alarmtiles.sleeptimer;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

import java.util.concurrent.TimeUnit;

@DataEnum
interface SleepTimerEvent_dataenum {

    dataenum_case Load();

    dataenum_case Loaded(SleepTimer sleepTimer);

    dataenum_case Toggle();

    dataenum_case Start();

    dataenum_case StartWith(SleepTimer sleepTimer);

    dataenum_case VolumeChanged(int volume);

    dataenum_case SetTime(double time);

    dataenum_case SetTimeUnit(TimeUnit timeUnit);

    dataenum_case SetDecreasingVolume(boolean decreasingVolume);

    dataenum_case Cancel();

    dataenum_case Finish();

    dataenum_case FinishWith(SleepTimer sleepTimer);
}