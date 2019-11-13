package linusfessler.alarmtiles.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.R;

public class QuickSettingsTile extends ConstraintLayout {

    private ColorStateList quickSettingsIconEnabledColor;
    private ColorStateList quickSettingsIconDisabledColor;
    private ColorStateList quickSettingsBackgroundEnabledColor;
    private ColorStateList quickSettingsBackgroundDisabledColor;

    private MaterialTextView nameView;
    private ImageView iconView;

    public QuickSettingsTile(@NonNull final Context context) {
        super(context);
        init(context, null);
    }

    public QuickSettingsTile(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public QuickSettingsTile(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        initSelf(context);
        inflateView(context);
        if (attrs != null) {
            initWithAttrs(context, attrs);
        }
    }

    private void initSelf(@NonNull final Context context) {
        setClickable(true);
        setFocusable(true);

        final Resources resources = context.getResources();
        final Resources.Theme theme = context.getTheme();

        this.quickSettingsIconEnabledColor = resources.getColorStateList(R.color.quickSettingsIconEnabled, theme);
        this.quickSettingsIconDisabledColor = resources.getColorStateList(R.color.quickSettingsIconDisabled, theme);
        this.quickSettingsBackgroundEnabledColor = resources.getColorStateList(R.color.quickSettingsBackgroundEnabled, theme);
        this.quickSettingsBackgroundDisabledColor = resources.getColorStateList(R.color.quickSettingsBackgroundDisabled, theme);
    }

    private void inflateView(@NonNull final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.quick_settings_tile, this);

        this.nameView = root.findViewById(R.id.name);
        this.iconView = root.findViewById(R.id.icon);
    }

    private void initWithAttrs(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.QuickSettingsTile);
        final boolean enabled = styledAttributes.getBoolean(R.styleable.QuickSettingsTile_enabled, false);
        final String name = styledAttributes.getString(R.styleable.QuickSettingsTile_name);
        final Drawable icon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon);
        styledAttributes.recycle();

        setEnabled(enabled);
        setName(name);
        setIcon(icon);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            this.iconView.setImageTintList(this.quickSettingsIconEnabledColor);
            this.iconView.setBackgroundTintList(this.quickSettingsBackgroundEnabledColor);
        } else {
            this.iconView.setImageTintList(this.quickSettingsIconDisabledColor);
            this.iconView.setBackgroundTintList(this.quickSettingsBackgroundDisabledColor);
        }
    }

    public void setName(final String name) {
        this.nameView.setText(name);
    }

    public void setIcon(final Drawable icon) {
        this.iconView.setImageDrawable(icon);
    }

}
