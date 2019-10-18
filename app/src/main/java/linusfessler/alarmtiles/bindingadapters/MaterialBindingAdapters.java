package linusfessler.alarmtiles.bindingadapters;

import androidx.databinding.BindingAdapter;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

public class MaterialBindingAdapters {

    private MaterialBindingAdapters() {
        throw new UnsupportedOperationException("Do not initialize this static class.");
    }

    @BindingAdapter("errorText")
    public static void setErrorText(final TextInputLayout view, final String errorText) {
        view.setError(errorText);
    }

    @BindingAdapter("checked")
    public static void setChecked(final SwitchMaterial view, final boolean checked) {
        view.setChecked(checked);
    }

}
