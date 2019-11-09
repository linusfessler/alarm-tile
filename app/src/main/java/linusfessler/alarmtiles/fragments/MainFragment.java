package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import linusfessler.alarmtiles.AlarmTilePageConfiguration;
import linusfessler.alarmtiles.AlarmTilePageFragmentAdapter;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager(view);
    }

    private void initViewPager(final View root) {
        final ViewPager2 viewPager = root.findViewById(R.id.view_pager);

        final int orientation = getResources().getConfiguration().orientation;
        final AlarmTilePageConfiguration pageConfiguration = AlarmTilePageConfiguration.fromOrientation(orientation);

        final AlarmTilePageFragmentAdapter adapter = new AlarmTilePageFragmentAdapter(this, pageConfiguration.getCount());
        viewPager.setAdapter(adapter);

        final AppDatabase db = AppDatabase.getInstance(requireContext());
        final LiveData<List<AlarmTile>> liveAlarmTiles = db.alarmTiles().selectAll();
        liveAlarmTiles.observeForever(adapter::setAlarmTiles);
    }

}
