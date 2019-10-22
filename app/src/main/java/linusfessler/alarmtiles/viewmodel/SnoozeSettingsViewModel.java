package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SnoozeSettingsViewModel extends ObservableViewModel {

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Setter
    private AlarmTile alarmTile;

    @Bindable
    public boolean isSnoozeEnabled() {
        return alarmTile.getSnoozeSettings().isSnoozeEnabled();
    }

    public void setSnoozeEnabled(final boolean snoozeEnabled) {
        alarmTile.getSnoozeSettings().setSnoozeEnabled(snoozeEnabled);
        notifyPropertyChanged(BR.snoozeEnabled);
    }

    @Bindable
    public int getSnoozeHours() {
        return alarmTile.getSnoozeSettings().getSnoozeHours();
    }

    public void setSnoozeHours(final int snoozeHours) {
        alarmTile.getSnoozeSettings().setSnoozeHours(snoozeHours);
        notifyPropertyChanged(BR.snoozeHours);
    }

    @Bindable
    public int getSnoozeMinutes() {
        return alarmTile.getSnoozeSettings().getSnoozeMinutes();
    }

    public void setSnoozeMinutes(final int snoozeMinutes) {
        alarmTile.getSnoozeSettings().setSnoozeMinutes(snoozeMinutes);
        notifyPropertyChanged(BR.snoozeMinutes);
    }

    @Bindable({"snoozeHours", "snoozeMinutes"})
    public String getSnoozeDuration() {
        return timeFormatter.format(getSnoozeHours(), getSnoozeMinutes());
    }

}
