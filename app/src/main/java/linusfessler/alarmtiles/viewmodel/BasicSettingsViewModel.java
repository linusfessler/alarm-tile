package linusfessler.alarmtiles.viewmodel;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import linusfessler.alarmtiles.R;
import lombok.Getter;

@Getter
public class BasicSettingsViewModel extends ObservableViewModel {

    public static final int DEFAULT_ICON_RESOURCE_ID = R.drawable.ic_alarm_24px;
    public static final int MAX_NAME_LENGTH = 20;
    public static final String NAME_ERROR_TEXT = "Please enter a name"; // TODO: Add to R.string

    @Bindable
    private String name;

    @Bindable
    private int iconResourceId = DEFAULT_ICON_RESOURCE_ID;

    @Bindable
    private boolean nameErrorEnabled = false;

    public void setName(final String name) {
        this.name = name;
        nameErrorEnabled = !isNameValid();
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.nameErrorEnabled);
    }

    public void setIconResourceId(final int iconResourceId) {
        this.iconResourceId = iconResourceId;
        notifyPropertyChanged(BR.iconResourceId);
    }

    @Bindable("name")
    public boolean isNameValid() {
        return name != null && !name.isEmpty();
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

    @Bindable("nameErrorEnabled")
    public String getNameErrorText() {
        return nameErrorEnabled ? NAME_ERROR_TEXT : null;
    }
}
