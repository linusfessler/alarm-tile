package linusfessler.alarmtiles.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import linusfessler.alarmtiles.AlarmTileRecyclerViewAdapter;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;

public class MainFragment extends Fragment {

    private static final int PORTRAIT_QUICK_SETTINGS_COLUMNS = 3;
    private static final int LANDSCAPE_QUICK_SETTINGS_COLUMNS = 4;

    private AlarmTileRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        initFab(view);
    }

    private void initRecyclerView(final View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

        final int orientation = getResources().getConfiguration().orientation;
        final int quickSettingsColumns;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            quickSettingsColumns = PORTRAIT_QUICK_SETTINGS_COLUMNS;
        } else {
            quickSettingsColumns = LANDSCAPE_QUICK_SETTINGS_COLUMNS;
        }
        final GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), quickSettingsColumns);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AlarmTileRecyclerViewAdapter(requireActivity(), this);
        recyclerView.setAdapter(adapter);

        final AppDatabase db = AppDatabase.getInstance(requireContext());
        final LiveData<List<AlarmTile>> liveAlarmTiles = db.alarmTiles().selectAll();
        liveAlarmTiles.observeForever(adapter::setAlarmTiles);
    }

    private void initFab(final View root) {
        final FloatingActionButton button = root.findViewById(R.id.fab);
        final AlarmTile alarmTile = new AlarmTile();
        final NavDirections direction = MainFragmentDirections.actionMainFragmentToSettingsContainerFragment(alarmTile);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(direction));
    }

    @Override
    public void onDestroyView() {
        adapter.dismissDeleteDialog();
        super.onDestroyView();
    }
}
