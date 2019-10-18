package linusfessler.alarmtiles.bindingadapters;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class MaterialBindingAdapters {

    private MaterialBindingAdapters() {
        throw new UnsupportedOperationException("Do not initialize this static class.");
    }

    @BindingAdapter("errorText")
    public static void setErrorMessage(final TextInputLayout view, final String errorMessage) {
        view.setError(errorMessage);
    }

}
