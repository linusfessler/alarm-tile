package linusfessler.alarmtiles.sleeptimer;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
interface SleepTimerEffect_dataenum {

    dataenum_case LoadFromDatabase();

    dataenum_case SaveToDatabase(SleepTimer sleepTimer);

    dataenum_case Start();

    dataenum_case Cancel();

    dataenum_case StartWith(SleepTimer sleepTimer);

    dataenum_case FinishWith(SleepTimer sleepTimer);

    dataenum_case StartDecreasingVolume(long millisLeft);

    dataenum_case StopDecreasingVolume();

    dataenum_case ScheduleFinish(long millisLeft);

    dataenum_case UnscheduleFinish();

    dataenum_case SetVolumeToZero();

    dataenum_case StopMediaPlayback();

    dataenum_case ShowNotification();

    dataenum_case HideNotification();
}