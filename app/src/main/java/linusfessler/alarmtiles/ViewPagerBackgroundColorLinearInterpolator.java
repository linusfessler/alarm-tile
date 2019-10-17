package linusfessler.alarmtiles;

import androidx.viewpager.widget.ViewPager;

public class ViewPagerBackgroundColorLinearInterpolator implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private ColorLinearInterpolator colorLinearInterpolator;

    public ViewPagerBackgroundColorLinearInterpolator(ViewPager viewPager, Integer... colors) {
        this.viewPager = viewPager;
        colorLinearInterpolator = new ColorLinearInterpolator(colors);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Integer backgroundColor = colorLinearInterpolator.getColor(position + positionOffset);
        viewPager.setBackgroundColor(backgroundColor);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
