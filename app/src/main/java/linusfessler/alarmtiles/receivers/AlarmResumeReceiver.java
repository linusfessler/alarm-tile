package linusfessler.alarmtiles.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import linusfessler.alarmtiles.schedulers.Schedulers;

public class AlarmResumeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction())
                || Intent.ACTION_MY_PACKAGE_REPLACED.equalsIgnoreCase(intent.getAction())) {
            Schedulers.getInstance(context).resume();
        }
    }
}
