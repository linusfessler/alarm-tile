package linusfessler.alarmtiles.schedulers;

import android.content.Context;

public class AlarmSchedulers {

    private AlarmSchedulers() {}

    private static Scheduler[] getSchedulers() {
        return new Scheduler[] {
                AlarmScheduler.getInstance(),
                TimerScheduler.getInstance(),
                SnoozeScheduler.getInstance()
        };
    }

    public static Scheduler getNextScheduler(Context context) {
        Scheduler[] schedulers = getSchedulers();
        Scheduler nextScheduler = schedulers[0];
        int nextDurationLeft = nextScheduler.getDurationLeft(context);
        for (int i = 1; i < schedulers.length; i++) {
            Scheduler scheduler = schedulers[i];
            if (!scheduler.isScheduled(context)) {
                continue;
            }
            int durationLeft = scheduler.getDurationLeft(context);
            if (durationLeft < nextDurationLeft) {
                nextScheduler = scheduler;
                nextDurationLeft = durationLeft;
            }
        }
        return nextScheduler;
    }

    public static boolean isAnyScheduled(Context context) {
        Scheduler[] schedulers = getSchedulers();
        for (Scheduler scheduler : schedulers) {
            if (scheduler.isScheduled(context)) {
                return true;
            }
        }
        return false;
    }

    public static void resume(Context context) {
        Scheduler[] schedulers = getSchedulers();
        for (int i = 0; i < schedulers.length; i++) {
            schedulers[i].resume(context);
        }
    }
}
