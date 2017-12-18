package linusfessler.alarmtiles;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import linusfessler.alarmtiles.fragments.AlarmFragment;
import linusfessler.alarmtiles.fragments.SchedulerFragment;
import linusfessler.alarmtiles.fragments.SnoozeFragment;
import linusfessler.alarmtiles.fragments.TimerFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Map<Integer, SchedulerFragment> fragments = new HashMap<>();

    public PagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    public SchedulerFragment getFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        SchedulerFragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AlarmFragment();
                break;
            case 1:
                fragment = new TimerFragment();
                break;
            case 2:
                fragment = new SnoozeFragment();
                break;
        }
        return fragment;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        fragments.put(position, (SchedulerFragment) getItem(position));
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
