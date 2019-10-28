package linusfessler.alarmtiles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpanningGridLayoutManager extends GridLayoutManager {

    private final int rowCount;

    public SpanningGridLayoutManager(final Context context, final int rowCount, final int columnCount) {
        super(context, columnCount);
        this.rowCount = rowCount;
    }

    public SpanningGridLayoutManager(final Context context, final int rowCount, final int columnCount, final int orientation, final boolean reverseLayout) {
        super(context, columnCount, orientation, reverseLayout);
        this.rowCount = rowCount;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return spanLayoutSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final Context c, final AttributeSet attrs) {
        return spanLayoutSize(super.generateLayoutParams(c, attrs));
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final ViewGroup.LayoutParams lp) {
        return spanLayoutSize(super.generateLayoutParams(lp));
    }

    private RecyclerView.LayoutParams spanLayoutSize(final RecyclerView.LayoutParams layoutParams) {
        if (getOrientation() == HORIZONTAL) {
            layoutParams.width = getHorizontalSpace(layoutParams) / rowCount;
        } else if (getOrientation() == VERTICAL) {
            layoutParams.height = getVerticalSpace(layoutParams) / rowCount;
        }
        return layoutParams;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    private int getHorizontalSpace(final RecyclerView.LayoutParams layoutParams) {
        return getWidth() - getPaddingRight() - getPaddingLeft() - layoutParams.getMarginStart() - layoutParams.getMarginEnd();
    }

    private int getVerticalSpace(final RecyclerView.LayoutParams layoutParams) {
        return getHeight() - getPaddingBottom() - getPaddingTop() - layoutParams.bottomMargin - layoutParams.topMargin;
    }

}