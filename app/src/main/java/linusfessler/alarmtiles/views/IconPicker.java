package linusfessler.alarmtiles.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.HashSet;
import java.util.Set;

import linusfessler.alarmtiles.R;

public class IconPicker extends FrameLayout implements View.OnClickListener {

    private final Set<OnIconChangedListener> listeners = new HashSet<>();
    private AlertDialog alertDialog;

    public IconPicker(@NonNull final Context context) {
        super(context);
        init(context, null, 0);
    }

    public IconPicker(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public IconPicker(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.icon_picker, this, true);
    }

    public void setIconResourceIds(final int[] iconResourceIds) {
        /*final LayoutInflater inflater = LayoutInflater.from(getContext());

        final ImageView selectedIconView = findViewById(R.id.selected_icon);
        final MaterialTextView hintView = findViewById(R.id.hint);

        selectedIconView.setVisibility(GONE);

        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Select an icon")
                .setCancelable(true)
                .create();

        final FlexboxLayout iconsView = (FlexboxLayout) inflater.inflate(R.layout.dialog_drawable_picker, this, false);

        for (final int iconResourceId : iconResourceIds) {
            final ImageView iconView = (ImageView) inflater.inflate(R.layout.icon, this, false);

            iconView.setImageResource(iconResourceId);
            iconView.setOnClickListener(v -> {
                iconView.setImageResource(iconResourceId);
                iconView.setVisibility(VISIBLE);
                hintView.setVisibility(GONE);

                for (final OnIconChangedListener listener : listeners) {
                    listener.onIconChanged(iconResourceId);
                }

                alertDialog.cancel();
            });

            iconsView.addView(iconView);
        }

        alertDialog.setView(iconsView);

        setOnClickListener(this);*/
    }

    @Override
    public void onClick(final View view) {
        Log.d("asdfjkasdlöfj", "asldkfjaöslfdk");
        alertDialog.show();
    }

    public interface OnIconChangedListener {
        void onIconChanged(int iconResourceId);
    }

}
