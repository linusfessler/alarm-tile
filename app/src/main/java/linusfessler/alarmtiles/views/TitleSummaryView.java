package linusfessler.alarmtiles.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.R;

public class TitleSummaryView extends LinearLayout {

    private MaterialTextView titleTextView;
    private MaterialTextView summaryTextView;

    public TitleSummaryView(@NonNull final Context context) {
        super(context);
        init(context, null);
    }

    public TitleSummaryView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleSummaryView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.title_summary_view, this);

        this.titleTextView = root.findViewById(R.id.title);
        this.summaryTextView = root.findViewById(R.id.summary);

        if (attrs == null) {
            return;
        }

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleSummaryView);
        final String title = styledAttributes.getString(R.styleable.TitleSummaryView_title);
        final String summary = styledAttributes.getString(R.styleable.TitleSummaryView_summary);
        styledAttributes.recycle();

        setTitle(title);
        setSummary(summary);
    }

    public void setTitle(final String title) {
        this.titleTextView.setText(title);
    }

    public void setSummary(final String summary) {
        this.summaryTextView.setText(summary);
    }
}
