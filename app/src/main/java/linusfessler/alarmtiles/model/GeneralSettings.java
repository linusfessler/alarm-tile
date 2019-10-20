package linusfessler.alarmtiles.model;

import java.io.Serializable;

import linusfessler.alarmtiles.Assert;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeneralSettings implements Serializable {

    private final String name;
    private final int iconResourceId;
    private final boolean showingNotification;
    private final boolean graduallyIncreasingVolume;
    private final boolean vibrating;
    private final boolean turningOnFlashlight;

    private GeneralSettings(final String name, final int iconResourceId,
                            final boolean showingNotification, final boolean graduallyIncreasingVolume, final boolean vibrating, final boolean turningOnFlashlight) {
        Assert.isNotEmpty(name, "Name must not be empty.");

        this.name = name;
        this.iconResourceId = iconResourceId;
        this.showingNotification = showingNotification;
        this.graduallyIncreasingVolume = graduallyIncreasingVolume;
        this.vibrating = vibrating;
        this.turningOnFlashlight = turningOnFlashlight;
    }

}
