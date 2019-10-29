package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GeneralSettingsViewModel extends ObservableViewModel {

    @Setter
    private AlarmTile alarmTile;

    @Bindable
    public String getName() {
        return alarmTile.getGeneralSettings().getName();
    }

    public void setName(final String name) {
        alarmTile.getGeneralSettings().setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getIconResourceName() {
        return alarmTile.getGeneralSettings().getIconResourceName();
    }

    public void setIconResourceName(final String iconResourceName) {
        alarmTile.getGeneralSettings().setIconResourceName(iconResourceName);
        notifyPropertyChanged(BR.iconResourceName);
    }

    @Bindable
    public boolean isShowingNotification() {
        return alarmTile.getGeneralSettings().isShowingNotification();
    }

    public void setShowingNotification(final boolean showingNotification) {
        alarmTile.getGeneralSettings().setShowingNotification(showingNotification);
        notifyPropertyChanged(BR.showingNotification);
    }

}
