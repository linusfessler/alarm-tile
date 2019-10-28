package linusfessler.alarmtiles;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Collections;
import java.util.List;

import linusfessler.alarmtiles.fragments.AlarmTilePageFragment;
import linusfessler.alarmtiles.model.AlarmTile;

public class AlarmTilePageFragmentAdapter extends FragmentStateAdapter {

    private final int alarmTilesPerPage;

    private List<AlarmTile> alarmTiles = Collections.emptyList();

    public AlarmTilePageFragmentAdapter(final Fragment fragment, final int alarmTilesPerPage) {
        super(fragment);
        this.alarmTilesPerPage = alarmTilesPerPage;
    }

    @NonNull
    @Override
    public Fragment createFragment(final int position) {
        return AlarmTilePageFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) alarmTiles.size() / alarmTilesPerPage);
    }

    public void setAlarmTiles(final List<AlarmTile> alarmTiles) {
        this.alarmTiles = alarmTiles;
        notifyDataSetChanged();
    }

}
