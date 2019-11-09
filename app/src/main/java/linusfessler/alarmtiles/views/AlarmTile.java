package linusfessler.alarmtiles.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.R;

public class AlarmTile extends FrameLayout {

    private ColorStateList quickSettingsIconEnabledColor;
    private ColorStateList quickSettingsIconDisabledColor;
    private ColorStateList quickSettingsBackgroundEnabledColor;
    private ColorStateList quickSettingsBackgroundDisabledColor;

    private MaterialTextView nameView;
    private ImageView iconView;

    public AlarmTile(@NonNull final Context context) {
        super(context);
        init(context, null);
    }

    public AlarmTile(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AlarmTile(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        initSelf(context);
        inflateView(context, attrs);
    }

    private void initSelf(final Context context) {
        setClickable(true);
        setFocusable(true);

        final Resources resources = context.getResources();
        final Resources.Theme theme = context.getTheme();

        quickSettingsIconEnabledColor = resources.getColorStateList(R.color.quickSettingsIconEnabled, theme);
        quickSettingsIconDisabledColor = resources.getColorStateList(R.color.quickSettingsIconDisabled, theme);
        quickSettingsBackgroundEnabledColor = resources.getColorStateList(R.color.quickSettingsBackgroundEnabled, theme);
        quickSettingsBackgroundDisabledColor = resources.getColorStateList(R.color.quickSettingsBackgroundDisabled, theme);
    }

    private void inflateView(final Context context, @Nullable final AttributeSet attrs) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.alarm_tile, this);

        nameView = root.findViewById(R.id.name);
        iconView = root.findViewById(R.id.icon);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AlarmTile);
        final boolean enabled = styledAttributes.getBoolean(R.styleable.AlarmTile_enabled, false);
        final String name = styledAttributes.getString(R.styleable.AlarmTile_name);
        final Drawable icon = styledAttributes.getDrawable(R.styleable.AlarmTile_icon);
        styledAttributes.recycle();

        setEnabled(enabled);
        setName(name);
        setIcon(icon);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            iconView.setImageTintList(quickSettingsIconEnabledColor);
            iconView.setBackgroundTintList(quickSettingsBackgroundEnabledColor);
        } else {
            iconView.setImageTintList(quickSettingsIconDisabledColor);
            iconView.setBackgroundTintList(quickSettingsBackgroundDisabledColor);
        }
    }

    public void setName(final String name) {
        nameView.setText(name);
    }

    public void setIcon(final Drawable icon) {
        iconView.setImageDrawable(icon);
    }

}
