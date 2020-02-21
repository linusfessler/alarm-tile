package linusfessler.alarmtiles.shared.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.textview.MaterialTextView
import linusfessler.alarmtiles.R

class TitleSummaryView : LinearLayout {
    private lateinit var titleTextView: MaterialTextView
    private lateinit var summaryTextView: MaterialTextView

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
        val inflater = LayoutInflater.from(context)
        val root = inflater.inflate(R.layout.title_summary_view, this)
        titleTextView = root.findViewById(R.id.title)
        summaryTextView = root.findViewById(R.id.summary)

        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleSummaryView)
            val title = styledAttributes.getString(R.styleable.TitleSummaryView_title)
            val summary = styledAttributes.getString(R.styleable.TitleSummaryView_summary)
            styledAttributes.recycle()

            title?.let {
                setTitle(it)
            }

            summary?.let {
                setSummary(it)
            }
        }
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setSummary(summary: String) {
        summaryTextView.text = summary
    }
}