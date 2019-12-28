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

    private ValueAnimator iconColorEnablingAnimator;
    private ValueAnimator iconColorDisablingAnimator;
    private ValueAnimator backgroundColorEnablingAnimator;
    private ValueAnimator backgroundColorDisablingAnimator;

    private MaterialTextView labelView;
    private MaterialTextView subtitleView;
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

        this.iconColorEnablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconDisabledColor, quickSettingsIconEnabledColor);
        this.iconColorDisablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconEnabledColor, quickSettingsIconDisabledColor);

        final ValueAnimator.AnimatorUpdateListener iconColorAnimatorUpdateListener = animator -> {
            final int iconColor = (int) animator.getAnimatedValue();
            this.iconView.setImageTintList(ColorStateList.valueOf(iconColor));
        };

        this.iconColorEnablingAnimator.addUpdateListener(iconColorAnimatorUpdateListener);
        this.iconColorDisablingAnimator.addUpdateListener(iconColorAnimatorUpdateListener);

        this.backgroundColorEnablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundDisabledColor, quickSettingsBackgroundEnabledColor);
        this.backgroundColorDisablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundEnabledColor, quickSettingsBackgroundDisabledColor);

        final ValueAnimator.AnimatorUpdateListener backgroundColorAnimatorUpdateListener = animator -> {
            final int backgroundColor = (int) animator.getAnimatedValue();
            this.iconView.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        };

        this.backgroundColorEnablingAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener);
        this.backgroundColorDisablingAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener);
    }

    private void inflateView(@NonNull final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.quick_settings_tile, this);

        this.labelView = root.findViewById(R.id.label);
        this.subtitleView = root.findViewById(R.id.subtitle);
        this.iconView = root.findViewById(R.id.icon);
    }

    private void initWithAttrs(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.QuickSettingsTile);
        final boolean enabled = styledAttributes.getBoolean(R.styleable.QuickSettingsTile_enabled, false);
        final String label = styledAttributes.getString(R.styleable.QuickSettingsTile_label);
        final String subtitle = styledAttributes.getString(R.styleable.QuickSettingsTile_subtitle);
        final Drawable icon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon);
        styledAttributes.recycle();

        this.setEnabled(enabled);
        this.setLabel(label);
        this.setSubtitle(subtitle);
        this.setIcon(icon);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            this.iconColorEnablingAnimator.start();
            this.backgroundColorEnablingAnimator.start();
        } else {
            this.iconColorDisablingAnimator.start();
            this.backgroundColorDisablingAnimator.start();
        }
    }

    public void setLabel(final String label) {
        this.labelView.setText(label);
    }

    public void setSubtitle(final String subtitle) {
        this.subtitleView.setText(subtitle);
    }

    public void setIcon(final Drawable icon) {
        this.iconView.setImageDrawable(icon);
    }
}
