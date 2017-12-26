package linusfessler.alarmtiles.schedulers;

import android.content.Context;

public class Schedulers {

    private static Schedulers instance;
    public static Schedulers getInstance(Context context) {
        if (instance == null) {
            instance = new Schedulers(context);
        }
        return instance;
    }

    private Scheduler[] schedulers;

    private Schedulers(Context context) {
        schedulers = new Scheduler[] {
                AlarmScheduler.getInstance(context),
                TimerScheduler.getInstance(context),
                SnoozeScheduler.getInstance(context)
        };
    }

    public void schedule() {
        for (int i = 0; i < schedulers.length; i++) {
            schedulers[i].dismiss();
        }
    }

    public boolean isScheduled() {
        for (Scheduler scheduler : schedulers) {
            if (scheduler.isScheduled()) {
                return true;
            }
        }
        return false;
    }

    public boolean alarmIsActive() {
        for (Scheduler scheduler : schedulers) {
            if (scheduler.alarmIsActive()) {
                return true;
            }
        }
        return false;
    }

    public Scheduler getCurrentScheduler() {
        for (Scheduler scheduler : schedulers) {
            if (scheduler.isScheduled()) {
                return scheduler;
            }
        }
        return null;
    }

    public void resume() {
        for (int i = 0; i < schedulers.length; i++) {
            schedulers[i].resume();
        }
    }

    /*public static Scheduler getNextScheduler(Context context) {
        Scheduler[] schedulers = getSchedulers();
        Scheduler nextScheduler = schedulers[0];
        int nextDurationLeft = nextScheduler.getDurationLeft(context);
        for (int i = 1; i < schedulers.length; i++) {
            Scheduler scheduler = schedulers[i];
            if (!scheduler.isAnyScheduled(context)) {
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
    }*/
}
