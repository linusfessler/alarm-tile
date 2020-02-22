package linusfessler.alarmtiles

import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class ViewPagerLinearBackgroundColorInterpolator(private val viewPager: ViewPager, vararg colors: Int) : OnPageChangeListener {
    private val linearColorInterpolator: LinearColorInterpolator = LinearColorInterpolator(*colors)

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val backgroundColor = linearColorInterpolator.getColor(position + positionOffset)
        viewPager.setBackgroundColor(backgroundColor)
    }

    override fun onPageSelected(position: Int) {
        // Nothing to do
    }

    override fun onPageScrollStateChanged(state: Int) {
        // Nothing to do
    }
}