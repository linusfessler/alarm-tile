package linusfessler.alarmtiles;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java.util.HashSet;
import java.util.Set;

public class DrawablePickerDialog {


    private final AlertDialog alertDialog;
    private final Set<OnDrawablePickedListener> listeners = new HashSet<>();

    private int[] resourceIds;

    public int[] getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(final int[] resourceIds) {
        this.resourceIds = resourceIds;
    }

    public DrawablePickerDialog(@NonNull final Context context, @NonNull final String title, @NonNull final int[] resourceIds,
                                final int drawableWidth, final int drawableHeight, final int drawableColor) {
        this.resourceIds = resourceIds;

        final FlexboxLayout flexboxLayout = new FlexboxLayout(context);
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);
        flexboxLayout.setJustifyContent(JustifyContent.FLEX_START);

        final TypedValue dialogPreferredPadding = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.dialogPreferredPadding, dialogPreferredPadding, true);
        final int dialogPreferredPaddingValue = context.getResources().getDimensionPixelSize(dialogPreferredPadding.resourceId);
        final int padding = 3 * dialogPreferredPaddingValue / 4;
        final int margin = dialogPreferredPaddingValue / 4;
        flexboxLayout.setPadding(padding, padding, padding, padding);

        for (final int resourceId : resourceIds) {
            final ImageView imageView = new ImageView(context);
            imageView.setImageResource(resourceId);

            final FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(drawableWidth, drawableHeight);
            layoutParams.setMargins(margin, margin, margin, margin);
            imageView.setLayoutParams(layoutParams);

            final TypedValue selectableItemBackgroundBorderless = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, selectableItemBackgroundBorderless, true);
            imageView.setBackgroundResource(selectableItemBackgroundBorderless.resourceId);

            final ColorStateList tint = ColorStateList.valueOf(drawableColor);
            imageView.setImageTintList(tint);

            imageView.setOnClickListener(view -> {
                for (final OnDrawablePickedListener listener : listeners) {
                    listener.onDrawablePicked(resourceId);
                }
                hide();
            });

            flexboxLayout.addView(imageView);
        }

        alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(flexboxLayout)
                .setCancelable(true)
                .create();
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.cancel();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public void addListener(final OnDrawablePickedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final OnDrawablePickedListener listener) {
        listeners.remove(listener);
    }

    public interface OnDrawablePickedListener {
        void onDrawablePicked(int resourceId);
    }

}
