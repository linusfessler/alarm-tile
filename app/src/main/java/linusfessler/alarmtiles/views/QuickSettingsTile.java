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
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.R;

public class QuickSettingsTile extends FrameLayout {

    private ValueAnimator iconColorEnablingAnimator;
    private ValueAnimator iconColorDisablingAnimator;
    private ValueAnimator backgroundColorEnablingAnimator;
    private ValueAnimator backgroundColorDisablingAnimator;

    private MaterialTextView labelView;
    private MaterialTextView subtitleView;
    private ImageView iconView;

    private boolean enabled;

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
        inflateView(context);
        initSelf(context);
        if (attrs != null) {
            initWithAttrs(context, attrs);
        }
    }

    private void initSelf(@NonNull final Context context) {
        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        final Resources resources = context.getResources();
        final Resources.Theme theme = context.getTheme();

        final int quickSettingsIconEnabledColor = resources.getColor(R.color.quickSettingsIconEnabled, theme);
        final int quickSettingsIconDisabledColor = resources.getColor(R.color.quickSettingsIconDisabled, theme);

        final int quickSettingsBackgroundEnabledColor = resources.getColor(R.color.quickSettingsBackgroundEnabled, theme);
        final int quickSettingsBackgroundDisabledColor = resources.getColor(R.color.quickSettingsBackgroundDisabled, theme);

        iconColorEnablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconDisabledColor, quickSettingsIconEnabledColor);
        iconColorDisablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconEnabledColor, quickSettingsIconDisabledColor);

        final ValueAnimator.AnimatorUpdateListener iconColorAnimatorUpdateListener = animator -> {
            final int iconColor = (int) animator.getAnimatedValue();
            iconView.setImageTintList(ColorStateList.valueOf(iconColor));
        };

        iconColorEnablingAnimator.addUpdateListener(iconColorAnimatorUpdateListener);
        iconColorDisablingAnimator.addUpdateListener(iconColorAnimatorUpdateListener);

        backgroundColorEnablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundDisabledColor, quickSettingsBackgroundEnabledColor);
        backgroundColorDisablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundEnabledColor, quickSettingsBackgroundDisabledColor);

        final ValueAnimator.AnimatorUpdateListener backgroundColorAnimatorUpdateListener = animator -> {
            final int backgroundColor = (int) animator.getAnimatedValue();
            iconView.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        };

        backgroundColorEnablingAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener);
        backgroundColorDisablingAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener);
    }

    private void inflateView(@NonNull final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.quick_settings_tile, this);

        labelView = root.findViewById(R.id.label);
        subtitleView = root.findViewById(R.id.subtitle);
        iconView = root.findViewById(R.id.icon);
    }

    private void initWithAttrs(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.QuickSettingsTile);
        final boolean initialEnabled = styledAttributes.getBoolean(R.styleable.QuickSettingsTile_enabled, false);
        final String initialLabel = styledAttributes.getString(R.styleable.QuickSettingsTile_label);
        final String initialSubtitle = styledAttributes.getString(R.styleable.QuickSettingsTile_subtitle);
        final Drawable initialIcon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon);
        styledAttributes.recycle();

        setEnabled(initialEnabled);
        setLabel(initialLabel);
        setSubtitle(initialSubtitle);
        setIcon(initialIcon);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }

        if (enabled) {
            iconColorEnablingAnimator.start();
            backgroundColorEnablingAnimator.start();
        } else {
            iconColorDisablingAnimator.start();
            backgroundColorDisablingAnimator.start();
        }

        this.enabled = enabled;
    }

    public void setLabel(final String label) {
        labelView.setText(label);
    }

    public void setSubtitle(final String subtitle) {
        subtitleView.setText(subtitle);
    }

    public void setIcon(final Drawable icon) {
        iconView.setImageDrawable(icon);
    }
}
