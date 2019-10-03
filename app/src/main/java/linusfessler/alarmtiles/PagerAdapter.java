package linusfessler.alarmtiles;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import linusfessler.alarmtiles.fragments.AlarmFragment;
import linusfessler.alarmtiles.fragments.SchedulerFragment;
import linusfessler.alarmtiles.fragments.SnoozeFragment;
import linusfessler.alarmtiles.fragments.TimerFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private List<SchedulerFragment> fragments = new ArrayList<>(3);

    public PagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
        fragments.add(position, (SchedulerFragment) getItem(position));
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
