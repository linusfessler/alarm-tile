package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import linusfessler.alarmtiles.AlarmTileListAdapter;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.Settings;
import linusfessler.alarmtiles.databinding.MainFragmentBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.BasicSettings;

public class MainFragment extends Fragment {

    private AppDatabase db;
    private Settings settings;
    private AlertDialog currentDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        db = ((App) requireActivity().getApplication()).getDb();
        settings = db.settingsDao().getSettings().getValue();

        final MainFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        binding.setSettings(settings);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        final NavController navController = Navigation.findNavController(view);

        final List<AlarmTile> alarmTiles = new ArrayList<>();

        final AlarmTile workweekAlarmTile = new AlarmTile();
        final AlarmTile weekendTimerTile = new AlarmTile();
        final AlarmTile napTile = new AlarmTile();

        final BasicSettings workweekBasicSettings = BasicSettings.builder()
                .name("Workweek")
                .iconResourceId(R.drawable.ic_alarm_24px)
                .build();
        final BasicSettings weekendBasicSettings = BasicSettings.builder()
                .name("Weekend")
                .iconResourceId(R.drawable.ic_timer_24px)
                .build();
        final BasicSettings napBasicSettings = BasicSettings.builder()
                .name("Nap")
                .iconResourceId(R.drawable.ic_snooze_24px)
                .build();

        workweekAlarmTile.setBasicSettings(workweekBasicSettings);
        weekendTimerTile.setBasicSettings(weekendBasicSettings);
        napTile.setBasicSettings(napBasicSettings);

        alarmTiles.add(workweekAlarmTile);
        alarmTiles.add(weekendTimerTile);
        alarmTiles.add(napTile);

        final AlarmTileListAdapter adapter = new AlarmTileListAdapter(getLayoutInflater(), alarmTiles);

        final ListView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, itemView, position, id) -> {
            if (currentDialog != null) {
                return;
            }

            final AlarmTile alarmTile = adapter.getItem(position);

            currentDialog = new AlertDialog.Builder(requireActivity())
                    .setTitle(alarmTile.getBasicSettings().getName())
                    .setMessage("Select an action for this alarm tile")
                    .setPositiveButton("Edit", (dialog, which) ->
                            navController.navigate(MainFragmentDirections.actionMainFragmentToBasicSettingsFragment(alarmTile))
                    )
                    .setNeutralButton("Delete", (dialog, which) -> {
                        alarmTiles.remove(alarmTile);
                        adapter.notifyDataSetChanged();
                    })
                    .setOnDismissListener(dialog -> currentDialog = null)
                    .create();

            currentDialog.show();
        });

        final FloatingActionButton button = view.findViewById(R.id.fab);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(MainFragmentDirections.actionMainFragmentToBasicSettingsFragment(new AlarmTile())));
    }

    @Override
    public void onDestroyView() {
        //db.settingsDao().update(settings);
        super.onDestroyView();
    }
}
