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

public class MainFragment extends Fragment {

    private AppDatabase db;
    private Settings settings;
    private AlertDialog currentDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = ((App) requireActivity().getApplication()).getDb();
        settings = db.settingsDao().getSettings().getValue();

        MainFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
        binding.setSettings(settings);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);

        List<AlarmTile> alarmTiles = new ArrayList<>();

        AlarmTile workweekAlarmTile = new AlarmTile();
        workweekAlarmTile.getBasicSettings().setName("Workweek");
        workweekAlarmTile.getBasicSettings().setIconResourceId(R.drawable.ic_alarm_24px);

        AlarmTile weekendTimerTile = new AlarmTile();
        weekendTimerTile.getBasicSettings().setName("Weekend");
        weekendTimerTile.getBasicSettings().setIconResourceId(R.drawable.ic_timer_24px);

        AlarmTile napTile = new AlarmTile();
        napTile.getBasicSettings().setName("Nap");
        napTile.getBasicSettings().setIconResourceId(R.drawable.ic_snooze_24px);

        alarmTiles.add(workweekAlarmTile);
        alarmTiles.add(weekendTimerTile);
        alarmTiles.add(napTile);

        AlarmTileListAdapter adapter = new AlarmTileListAdapter(getLayoutInflater(), alarmTiles);

        ListView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, itemView, position, id) -> {
            if (currentDialog != null) {
                return;
            }

            AlarmTile alarmTile = adapter.getItem(position);

            currentDialog = new AlertDialog.Builder(requireActivity())
                    .setTitle(alarmTile.getBasicSettings().getName())
                    .setMessage("Select an action for this alarm tile")
                    .setPositiveButton("Edit", (dialog, which) ->
                            navController.navigate(MainFragmentDirections.actionMainFragmentToNewAlarmFragment(alarmTile))
                    )
                    .setNeutralButton("Delete", (dialog, which) -> {
                        alarmTiles.remove(alarmTile);
                        adapter.notifyDataSetChanged();
                    })
                    .setOnDismissListener(dialog -> currentDialog = null)
                    .create();

            currentDialog.show();
        });

        FloatingActionButton button = view.findViewById(R.id.fab);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(MainFragmentDirections.actionMainFragmentToNewAlarmFragment(new AlarmTile())));
    }

    @Override
    public void onDestroyView() {
        //db.settingsDao().update(settings);
        super.onDestroyView();
    }
}
