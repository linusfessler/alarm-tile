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

    public TitleSummaryView(@NonNull final Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public TitleSummaryView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public TitleSummaryView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public TitleSummaryView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleRes, defStyleRes);
    }

    public void init(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View root = inflater.inflate(R.layout.title_summary_view, this);
        final MaterialTextView titleTextView = root.findViewById(R.id.title);
        final MaterialTextView summaryTextView = root.findViewById(R.id.summary);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleSummaryView);
        final String title = styledAttributes.getString(R.styleable.TitleSummaryView_title);
        final String summary = styledAttributes.getString(R.styleable.TitleSummaryView_summary);
        styledAttributes.recycle();

        titleTextView.setText(title);
        summaryTextView.setText(summary);
    }

}
