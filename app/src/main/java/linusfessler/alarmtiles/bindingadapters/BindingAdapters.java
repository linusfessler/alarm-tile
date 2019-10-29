package linusfessler.alarmtiles.bindingadapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    private BindingAdapters() {
        throw new UnsupportedOperationException("Do not initialize this static class.");
    }

    @BindingAdapter("src")
    public static void setImageResource(final ImageView imageView, final int resourceId) {
        imageView.setImageResource(resourceId);
    }

    @BindingAdapter("src")
    public static void setImageResource(final ImageView imageView, final String resourceName) {
        final int resourceId = imageView.getResources().getIdentifier(resourceName, "drawable", imageView.getContext().getPackageName());
        setImageResource(imageView, resourceId);
    }

}
