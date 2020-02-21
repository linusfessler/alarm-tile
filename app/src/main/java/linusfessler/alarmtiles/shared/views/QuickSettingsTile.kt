package linusfessler.alarmtiles.shared.views

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.google.android.material.textview.MaterialTextView
import linusfessler.alarmtiles.R

class QuickSettingsTile : FrameLayout {
    private lateinit var iconColorEnablingAnimator: ValueAnimator
    private lateinit var iconColorDisablingAnimator: ValueAnimator
    private lateinit var backgroundColorEnablingAnimator: ValueAnimator
    private lateinit var backgroundColorDisablingAnimator: ValueAnimator
    private lateinit var labelView: MaterialTextView
    private lateinit var subtitleView: MaterialTextView
    private lateinit var iconView: ImageView

    private var isEnabled: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflateView(context)
        initSelf(context)
        attrs?.let {
            initWithAttrs(context, it)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initSelf(context: Context) {
        val argbEvaluator = ArgbEvaluator()
        val resources = context.resources
        val theme = context.theme

        val quickSettingsIconEnabledColor = resources.getColor(R.color.quickSettingsIconEnabled, theme)
        val quickSettingsIconDisabledColor = resources.getColor(R.color.quickSettingsIconDisabled, theme)
        val quickSettingsBackgroundEnabledColor = resources.getColor(R.color.quickSettingsBackgroundEnabled, theme)
        val quickSettingsBackgroundDisabledColor = resources.getColor(R.color.quickSettingsBackgroundDisabled, theme)

        iconColorEnablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconDisabledColor, quickSettingsIconEnabledColor)
        iconColorDisablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsIconEnabledColor, quickSettingsIconDisabledColor)
        val iconColorAnimatorUpdateListener = AnimatorUpdateListener { animator: ValueAnimator ->
            val iconColor = animator.animatedValue as Int
            iconView.imageTintList = ColorStateList.valueOf(iconColor)
        }
        iconColorEnablingAnimator.addUpdateListener(iconColorAnimatorUpdateListener)
        iconColorDisablingAnimator.addUpdateListener(iconColorAnimatorUpdateListener)

        backgroundColorEnablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundDisabledColor, quickSettingsBackgroundEnabledColor)
        backgroundColorDisablingAnimator = ValueAnimator.ofObject(argbEvaluator, quickSettingsBackgroundEnabledColor, quickSettingsBackgroundDisabledColor)
        val backgroundColorAnimatorUpdateListener = AnimatorUpdateListener { animator: ValueAnimator ->
            val backgroundColor = animator.animatedValue as Int
            iconView.backgroundTintList = ColorStateList.valueOf(backgroundColor)
        }
        backgroundColorEnablingAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener)
        backgroundColorDisablingAnimator.addUpdateListener(backgroundColorAnimatorUpdateListener)
    }

    private fun inflateView(context: Context) {
        val inflater = LayoutInflater.from(context)
        val root = inflater.inflate(R.layout.quick_settings_tile, this)
        labelView = root.findViewById(R.id.label)
        subtitleView = root.findViewById(R.id.subtitle)
        iconView = root.findViewById(R.id.icon)
    }

    private fun initWithAttrs(context: Context, attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.QuickSettingsTile)
        val isEnabled = styledAttributes.getBoolean(R.styleable.QuickSettingsTile_enabled, false)
        val label = styledAttributes.getString(R.styleable.QuickSettingsTile_label)
        val subtitle = styledAttributes.getString(R.styleable.QuickSettingsTile_subtitle)
        val icon = styledAttributes.getDrawable(R.styleable.QuickSettingsTile_icon)
        styledAttributes.recycle()

        this.isEnabled = isEnabled

        label?.let {
            setLabel(it)
        }

        subtitle?.let {
            setSubtitle(it)
        }

        icon?.let {
            setIcon(it)
        }
    }

    override fun isEnabled() = isEnabled

    override fun setEnabled(enabled: Boolean) {
        if (enabled == isEnabled) {
            return
        }

        if (enabled) {
            iconColorEnablingAnimator.start()
            backgroundColorEnablingAnimator.start()
        } else {
            iconColorDisablingAnimator.start()
            backgroundColorDisablingAnimator.start()
        }

        this.isEnabled = enabled
    }

    fun setLabel(label: String) {
        labelView.text = label
    }

    fun setSubtitle(subtitle: String) {
        subtitleView.text = subtitle
    }

    fun setIcon(icon: Drawable) {
        iconView.setImageDrawable(icon)
    }
}