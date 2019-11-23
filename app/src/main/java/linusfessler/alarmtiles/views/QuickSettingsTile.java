package linusfessler.alarmtiles.views;

import android.animation.ValueAnimator;
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
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.R;

public class QuickSettingsTile extends ConstraintLayout {

    private ValueAnimator iconColorAnimation;
    private ValueAnimator backgroundColorAnimation;

    private MaterialTextView nameView;
    private ImageView iconView;

    public QuickSettingsTile(@NonNull final Context context) {
        super(context);
        this.init(context, null);
    }

    public QuickSettingsTile(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public QuickSettingsTile(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this.inflateView(context);
        this.initSelf(context);
        if (attrs != null) {
            this.initWithAttrs(context, attrs);
        }
    }

    private void initSelf(@NonNull final Context context) {
        this.setClickable(true);
        this.setFocusable(true);

        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        final Resources resources = context.getResources();
        final Resources.Theme theme = context.getTheme();

        final int quickSettingsIconEnabledColor = resources.getColor(R.color.quickSettingsIconEnabled, theme);
        final int quickSettingsIconDisabledColor = resources.getColor(R.color.quickSettingsIconDisabled, theme);

        final int quickSettingsBackgroundEnabledColor = resources.getColor(R.color.quickSettingsBackgroundEnabled, theme);
        final int quickSettingsBackgroundDisabledColor = resources.getColor(R.color.quickSettingsBackgroundDisabled, theme);

        this.iconColorAnimation = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconDisabledColor, quickSettingsIconEnabledColor);
        this.iconColorAnimation.addUpdateListener(animator -> {
            final int iconColor = (int) animator.getAnimatedValue();
            this.iconView.setImageTintList(ColorStateList.valueOf(iconColor));
        });

        this.backgroundColorAnimation = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundDisabledColor, quickSettingsBackgroundEnabledColor);
        this.backgroundColorAnimation.addUpdateListener(animator -> {
            final int backgroundColor = (int) animator.getAnimatedValue();
            this.iconView.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        });
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

        this.setEnabled(enabled);
        this.setName(name);
        this.setIcon(icon);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            this.iconColorAnimation.start();
            this.backgroundColorAnimation.start();
        } else {
            this.iconColorAnimation.reverse();
            this.backgroundColorAnimation.reverse();
        }
    }

    public void setName(final String name) {
        this.nameView.setText(name);
    }

    public void setIcon(final Drawable icon) {
        this.iconView.setImageDrawable(icon);
    }

}
