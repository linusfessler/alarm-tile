package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.model.SnoozeSettings;
import lombok.Getter;

@Getter
public class SnoozeSettingsViewModel extends ObservableViewModel {

    private static final int DEFAULT_SNOOZE_HOURS = 0;
    private static final int DEFAULT_SNOOZE_MINUTES = 15;

    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Bindable
    private boolean snoozeEnabled;

    @Bindable
    private int snoozeHours;

    @Bindable
    private int snoozeMinutes;

    public void setSnoozeEnabled(final boolean snoozeEnabled) {
        this.snoozeEnabled = snoozeEnabled;
        notifyPropertyChanged(BR.snoozeEnabled);
    }

    public void setSnoozeHours(final int snoozeHours) {
        this.snoozeHours = snoozeHours;
        notifyPropertyChanged(BR.snoozeHours);
    }

    public void setSnoozeMinutes(final int snoozeMinutes) {
        this.snoozeMinutes = snoozeMinutes;
        notifyPropertyChanged(BR.snoozeMinutes);
    }

    @Bindable({"snoozeHours", "snoozeMinutes"})
    public String getSnoozeDuration() {
        return timeFormatter.format(snoozeHours, snoozeMinutes);
    }

    public void reset() {
        setSnoozeEnabled(false);
        setSnoozeHours(DEFAULT_SNOOZE_HOURS);
        setSnoozeMinutes(DEFAULT_SNOOZE_MINUTES);
    }

    public void init(final SnoozeSettings snoozeSettings) {
        setSnoozeEnabled(snoozeSettings.isSnoozeEnabled());
        setSnoozeHours(snoozeSettings.getSnoozeHours());
        setSnoozeMinutes(snoozeSettings.getSnoozeMinutes());
    }

    public SnoozeSettings getSnoozeSettings() {
        return SnoozeSettings.builder()
                .snoozeEnabled(isSnoozeEnabled())
                .snoozeHours(getSnoozeHours())
                .snoozeMinutes(getSnoozeMinutes())
                .build();
    }

}
