package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.TimeOfDayFormatter;
import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WakeUpSettingsViewModel extends ObservableViewModel {

    private final TimeOfDayFormatter timeOfDayFormatter = new TimeOfDayFormatter();

    @Setter
    private AlarmTile alarmTile;

    @Setter
    private boolean is24Hours;

    @Bindable
    public boolean isAlarmEnabled() {
        return alarmTile.getWakeUpSettings().isAlarmEnabled();
    }

    public void setAlarmEnabled(final boolean alarmEnabled) {
        alarmTile.getWakeUpSettings().setAlarmEnabled(alarmEnabled);
        notifyPropertyChanged(BR.alarmEnabled);
    }

    @Bindable
    public int getAlarmHour() {
        return alarmTile.getWakeUpSettings().getAlarmHour();
    }

    public void setAlarmHour(final int alarmHour) {
        alarmTile.getWakeUpSettings().setAlarmHour(alarmHour);
        notifyPropertyChanged(BR.alarmHour);
    }

    @Bindable
    public int getAlarmMinute() {
        return alarmTile.getWakeUpSettings().getAlarmMinute();
    }

    public void setAlarmMinute(final int alarmMinute) {
        alarmTile.getWakeUpSettings().setAlarmMinute(alarmMinute);
        notifyPropertyChanged(BR.alarmMinute);
    }

    @Bindable("alarmEnabled")
    public boolean isShowingBothTimerAndAlarmInfo() {
        return isAlarmEnabled() && alarmTile.getSleepSettings().isTimerEnabled();
    }

    @Bindable({"alarmHour", "alarmMinute"})
    public String getAlarmTime() {
        return timeOfDayFormatter.format(getAlarmHour(), getAlarmMinute(), is24Hours);
    }

}
