package linusfessler.alarmtiles;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import linusfessler.alarmtiles.fragments.FallingAsleepFragment;
import linusfessler.alarmtiles.timer.TimerFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FallingAsleepFragment();
            case 1:
                return TimerFragment.newInstance();
            default:
                throw new IllegalStateException("Should never happen");
        }
    }

}
