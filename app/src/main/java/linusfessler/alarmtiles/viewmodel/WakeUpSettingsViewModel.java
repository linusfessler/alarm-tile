package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeOfDayFormatter;
import linusfessler.alarmtiles.model.WakeUpSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WakeUpSettingsViewModel extends ObservableViewModel {

    private static final int DEFAULT_ALARM_HOUR = 8;
    private static final int DEFAULT_ALARM_MINUTE = 0;

    private final TimeOfDayFormatter timeOfDayFormatter = new TimeOfDayFormatter();

    @Setter
    private boolean is24Hours;

    @Bindable
    private boolean alarmEnabled;

    @Bindable
    private int alarmHour;

    @Bindable
    private int alarmMinute;

    @Bindable
    private boolean sleepSettingsTimerEnabled;

    public void setAlarmEnabled(final boolean alarmEnabled) {
        this.alarmEnabled = alarmEnabled;
        notifyPropertyChanged(BR.alarmEnabled);
    }

    public void setAlarmHour(final int alarmHour) {
        this.alarmHour = alarmHour;
        notifyPropertyChanged(BR.alarmHour);
    }

    public void setAlarmMinute(final int alarmMinute) {
        this.alarmMinute = alarmMinute;
        notifyPropertyChanged(BR.alarmMinute);
    }

    public void setSleepSettingsTimerEnabled(final boolean sleepSettingsTimerEnabled) {
        this.sleepSettingsTimerEnabled = sleepSettingsTimerEnabled;
        notifyPropertyChanged(BR.sleepSettingsTimerEnabled);
    }

    @Bindable({"alarmEnabled", "sleepSettingsTimerEnabled"})
    public boolean isShowingBothTimerAndAlarmInfo() {
        return alarmEnabled && sleepSettingsTimerEnabled;
    }

    @Bindable({"alarmHour", "alarmMinute"})
    public String getAlarmTime() {
        return timeOfDayFormatter.format(alarmHour, alarmMinute, is24Hours);
    }

    public void reset() {
        setAlarmEnabled(false);
        setAlarmHour(DEFAULT_ALARM_HOUR);
        setAlarmMinute(DEFAULT_ALARM_MINUTE);
    }

    public void init(final WakeUpSettings wakeUpSettings) {
        setAlarmEnabled(wakeUpSettings.isAlarmEnabled());
        setAlarmHour(wakeUpSettings.getAlarmHour());
        setAlarmMinute(wakeUpSettings.getAlarmMinute());
    }

}
