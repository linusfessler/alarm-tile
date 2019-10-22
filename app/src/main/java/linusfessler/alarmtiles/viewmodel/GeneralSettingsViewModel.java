package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.model.AlarmTile;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GeneralSettingsViewModel extends ObservableViewModel {

    public static final int MAX_NAME_LENGTH = 20;
    public static final String NAME_ERROR_TEXT = "Please enter a name";

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
    public int getIconResourceId() {
        return alarmTile.getGeneralSettings().getIconResourceId();
    }

    public void setIconResourceId(final int iconResourceId) {
        alarmTile.getGeneralSettings().setIconResourceId(iconResourceId);
        notifyPropertyChanged(BR.iconResourceId);
    }

    @Bindable
    public boolean isShowingNotification() {
        return alarmTile.getGeneralSettings().isShowingNotification();
    }

    public void setShowingNotification(final boolean showingNotification) {
        alarmTile.getGeneralSettings().setShowingNotification(showingNotification);
        notifyPropertyChanged(BR.showingNotification);
    }

    @Bindable
    public boolean isGraduallyIncreasingVolume() {
        return alarmTile.getGeneralSettings().isGraduallyIncreasingVolume();
    }

    public void setGraduallyIncreasingVolume(final boolean graduallyIncreasingVolume) {
        alarmTile.getGeneralSettings().setGraduallyIncreasingVolume(graduallyIncreasingVolume);
        notifyPropertyChanged(BR.graduallyIncreasingVolume);
    }

    @Bindable
    public boolean isVibrating() {
        return alarmTile.getGeneralSettings().isVibrating();
    }

    public void setVibrating(final boolean vibrating) {
        alarmTile.getGeneralSettings().setVibrating(vibrating);
        notifyPropertyChanged(BR.vibrating);
    }

    @Bindable
    public boolean isTurningOnFlashlight() {
        return alarmTile.getGeneralSettings().isTurningOnFlashlight();
    }

    public void setTurningOnFlashlight(final boolean turningOnFlashlight) {
        alarmTile.getGeneralSettings().setTurningOnFlashlight(turningOnFlashlight);
        notifyPropertyChanged(BR.turningOnFlashlight);
    }

    @Bindable("name")
    public boolean isNameValid() {
        return getName() != null && !getName().isEmpty();
    }

    @Bindable("name")
    public boolean isNameErrorEnabled() {
        return getName() != null && getName().isEmpty();
    }

    @Bindable("name")
    public String getNameErrorText() {
        return isNameErrorEnabled() ? NAME_ERROR_TEXT : null;
    }

    @Bindable("iconResourceId")
    public boolean isIconResourceIdValid() {
        return true;
    }

    @Bindable({"name", "iconResourceId"})
    public boolean isValid() {
        return isNameValid() && isIconResourceIdValid();
    }

    @Bindable
    public int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

}
