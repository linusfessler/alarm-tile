package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WakeUpSettings implements Serializable {

    private boolean alarmEnabled;
    private int alarmHour;
    private int alarmMinute;

    private boolean timerEnabled;
    private int timerHours;
    private int timerMinutes;

    private boolean snoozeEnabled;
    private int snoozeHours;
    private int snoozeMinutes;

    private boolean volumeTimerEnabled;
    private int volumeTimerHours;
    private int volumeTimerMinutes;

    private boolean dismissTimerEnabled;
    private int dismissTimerHours;
    private int dismissTimerMinutes;

    private boolean vibrating;
    private boolean turningOnFlashlight;

    @Ignore
    public WakeUpSettings() {
        setAlarmEnabled(false);
        setAlarmHour(8);
        setAlarmMinute(0);

        setTimerEnabled(false);
        setTimerHours(8);
        setTimerMinutes(0);

        setSnoozeEnabled(false);
        setSnoozeHours(0);
        setSnoozeMinutes(15);

        setVolumeTimerEnabled(false);
        setVolumeTimerHours(0);
        setVolumeTimerMinutes(5);

        setDismissTimerEnabled(false);
        setDismissTimerHours(0);
        setDismissTimerMinutes(10);

        setVibrating(false);
        setTurningOnFlashlight(false);
    }

    public WakeUpSettings(final boolean alarmEnabled, final int alarmHour, final int alarmMinute,
                          final boolean timerEnabled, final int timerHours, final int timerMinutes,
                          final boolean snoozeEnabled, final int snoozeHours, final int snoozeMinutes,
                          final boolean volumeTimerEnabled, final int volumeTimerHours, final int volumeTimerMinutes,
                          final boolean dismissTimerEnabled, final int dismissTimerHours, final int dismissTimerMinutes,
                          final boolean vibrating, final boolean turningOnFlashlight) {
        this.alarmEnabled = alarmEnabled;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;

        this.timerEnabled = timerEnabled;
        this.timerHours = timerHours;
        this.timerMinutes = timerMinutes;

        this.snoozeEnabled = snoozeEnabled;
        this.snoozeHours = snoozeHours;
        this.snoozeMinutes = snoozeMinutes;

        this.volumeTimerEnabled = volumeTimerEnabled;
        this.volumeTimerHours = volumeTimerHours;
        this.volumeTimerMinutes = volumeTimerMinutes;

        this.dismissTimerEnabled = dismissTimerEnabled;
        this.dismissTimerHours = dismissTimerHours;
        this.dismissTimerMinutes = dismissTimerMinutes;

        this.vibrating = vibrating;
        this.turningOnFlashlight = turningOnFlashlight;
    }

}
