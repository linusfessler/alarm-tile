package linusfessler.alarmtiles;

import androidx.viewpager.widget.ViewPager;

public class ViewPagerLinearBackgroundColorInterpolator implements ViewPager.OnPageChangeListener {

    private final ViewPager viewPager;
    private final LinearColorInterpolator linearColorInterpolator;

    public ViewPagerLinearBackgroundColorInterpolator(final ViewPager viewPager, final int... colors) {
        this.viewPager = viewPager;
        linearColorInterpolator = new LinearColorInterpolator(colors);
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        final int backgroundColor = linearColorInterpolator.getColor(position + positionOffset);
        viewPager.setBackgroundColor(backgroundColor);
    }

    @Override
    public void onPageSelected(final int position) {
        // Nothing to do
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        // Nothing to do
    }
}
