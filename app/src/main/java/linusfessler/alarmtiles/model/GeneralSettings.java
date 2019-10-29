package linusfessler.alarmtiles.model;

import android.content.Context;

import androidx.room.Ignore;

import java.io.Serializable;

import linusfessler.alarmtiles.R;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralSettings implements Serializable {

    private String name;
    private String iconResourceName;
    private boolean showingNotification;
    private boolean volumeTimerEnabled;
    private int volumeTimerHours;
    private int volumeTimerMinutes;
    private boolean dismissTimerEnabled;
    private int dismissTimerHours;
    private int dismissTimerMinutes;
    private boolean vibrating;
    private boolean turningOnFlashlight;

    @Ignore
    public GeneralSettings(final Context context) {
        final String defaultIconResourceName = context.getResources().getResourceEntryName(R.drawable.ic_alarm_24px);

        setName("");
        setIconResourceName(defaultIconResourceName);
        setShowingNotification(true);
        setVolumeTimerEnabled(false);
        setVolumeTimerHours(0);
        setVolumeTimerMinutes(5);
        setDismissTimerEnabled(false);
        setDismissTimerHours(0);
        setDismissTimerMinutes(10);
        setVibrating(false);
        setTurningOnFlashlight(false);
    }

    public GeneralSettings(final String name, final String iconResourceName, final boolean showingNotification, final boolean volumeTimerEnabled, final int volumeTimerHours, final int volumeTimerMinutes, final boolean dismissTimerEnabled, final int dismissTimerHours, final int dismissTimerMinutes, final boolean vibrating, final boolean turningOnFlashlight) {
        this.name = name;
        this.iconResourceName = iconResourceName;
        this.showingNotification = showingNotification;
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
