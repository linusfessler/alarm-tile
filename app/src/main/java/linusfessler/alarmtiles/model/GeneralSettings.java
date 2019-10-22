package linusfessler.alarmtiles.model;

import androidx.room.Ignore;

import linusfessler.alarmtiles.R;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralSettings {

    private String name;
    private int iconResourceId;
    private boolean showingNotification;
    private boolean graduallyIncreasingVolume;
    private boolean vibrating;
    private boolean turningOnFlashlight;

    @Ignore
    public GeneralSettings() {
        setName(null);
        setIconResourceId(R.drawable.ic_alarm_24px);
        setShowingNotification(true);
        setGraduallyIncreasingVolume(false);
        setVibrating(false);
        setTurningOnFlashlight(false);
    }

    public GeneralSettings(final String name, final int iconResourceId, final boolean showingNotification, final boolean graduallyIncreasingVolume, final boolean vibrating, final boolean turningOnFlashlight) {
        this.name = name;
        this.iconResourceId = iconResourceId;
        this.showingNotification = showingNotification;
        this.graduallyIncreasingVolume = graduallyIncreasingVolume;
        this.vibrating = vibrating;
        this.turningOnFlashlight = turningOnFlashlight;
    }

}
