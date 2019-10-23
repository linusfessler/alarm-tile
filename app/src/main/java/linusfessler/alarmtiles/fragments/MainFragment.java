package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import linusfessler.alarmtiles.AlarmTileListAdapter;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.GeneralSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SnoozeSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.WakeUpSettingsViewModel;

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
        final NavController navController = Navigation.findNavController(view);

        final AppDatabase db = AppDatabase.getInstance(requireContext());

        final LiveData<List<AlarmTile>> liveAlarmTiles = db.alarmTiles().selectAll();
        final AlarmTileListAdapter adapter = new AlarmTileListAdapter(requireContext());
        liveAlarmTiles.observeForever(adapter::setAlarmTiles);

        final ListView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, itemView, position, id) -> {
            final AlarmTile alarmTile = adapter.getItem(position);
            initViewModels(alarmTile);
            navController.navigate(MainFragmentDirections.actionMainFragmentToGeneralSettingsFragment());
        });

        final FloatingActionButton button = view.findViewById(R.id.fab);
        button.setOnClickListener(v -> {
            final AlarmTile alarmTile = new AlarmTile();
            initViewModels(alarmTile);
            navController.navigate(MainFragmentDirections.actionMainFragmentToGeneralSettingsFragment());
        });
    }

    private void initViewModels(final AlarmTile alarmTile) {
        final GeneralSettingsViewModel generalSettingsViewModel = ViewModelProviders.of(requireActivity()).get(GeneralSettingsViewModel.class);
        final FallAsleepSettingsViewModel fallAsleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);
        final WakeUpSettingsViewModel wakeUpSettingsViewModel = ViewModelProviders.of(requireActivity()).get(WakeUpSettingsViewModel.class);
        final SnoozeSettingsViewModel snoozeSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SnoozeSettingsViewModel.class);

        generalSettingsViewModel.setAlarmTile(alarmTile);
        fallAsleepSettingsViewModel.setAlarmTile(alarmTile);
        sleepSettingsViewModel.setAlarmTile(alarmTile);
        wakeUpSettingsViewModel.setAlarmTile(alarmTile);
        snoozeSettingsViewModel.setAlarmTile(alarmTile);
    }

}
