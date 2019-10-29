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

    @Ignore
    public GeneralSettings(final Context context) {
        final String defaultIconResourceName = context.getResources().getResourceEntryName(R.drawable.ic_alarm_24px);

        setName("");
        setIconResourceName(defaultIconResourceName);
        setShowingNotification(true);
    }

    public GeneralSettings(final String name, final String iconResourceName, final boolean showingNotification) {
        this.name = name;
        this.iconResourceName = iconResourceName;
        this.showingNotification = showingNotification;
    }

}
