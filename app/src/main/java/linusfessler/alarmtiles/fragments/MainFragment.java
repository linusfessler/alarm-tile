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
import androidx.lifecycle.ViewModelProviders;
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
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.BasicSettings;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.viewmodel.BasicSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;

public class MainFragment extends Fragment {

    private AppDatabase db;
    private Settings settings;
    private AlertDialog currentDialog;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = ((App) requireActivity().getApplication()).getDb();
        settings = db.settingsDao().getSettings().getValue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.setSettings(settings);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        final List<AlarmTile> alarmTiles = new ArrayList<>();

        // TODO: Insert into db on install and fetch instead of building here
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

        final FallAsleepSettings workweekFallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .slowlyFadingMusicOut(true)
                .build();
        final FallAsleepSettings weekendFallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .slowlyFadingMusicOut(true)
                .build();
        final FallAsleepSettings napFallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .slowlyFadingMusicOut(true)
                .build();

        workweekAlarmTile.setFallAsleepSettings(workweekFallAsleepSettings);
        weekendTimerTile.setFallAsleepSettings(weekendFallAsleepSettings);
        napTile.setFallAsleepSettings(napFallAsleepSettings);

        final SleepSettings workweekSleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .hours(0)
                .minutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();
        final SleepSettings weekendSleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .hours(0)
                .minutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();
        final SleepSettings napSleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .hours(0)
                .minutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();

        workweekAlarmTile.setSleepSettings(workweekSleepSettings);
        weekendTimerTile.setSleepSettings(weekendSleepSettings);
        napTile.setSleepSettings(napSleepSettings);

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
                    .setPositiveButton("Edit", (dialog, which) -> {
                        initViewModels(alarmTile);
                        navController.navigate(MainFragmentDirections.actionMainFragmentToBasicSettingsFragment());
                    })
                    .setNeutralButton("Delete", (dialog, which) -> {
                        alarmTiles.remove(alarmTile);
                        adapter.notifyDataSetChanged();
                    })
                    .setOnDismissListener(dialog -> currentDialog = null)
                    .create();

            currentDialog.show();
        });

        final FloatingActionButton button = view.findViewById(R.id.fab);
        button.setOnClickListener(v -> {
            resetViewModels();
            navController.navigate(MainFragmentDirections.actionMainFragmentToBasicSettingsFragment());
        });
    }

    private void initViewModels(final AlarmTile alarmTile) {
        final BasicSettingsViewModel basicSettingsViewModel = ViewModelProviders.of(requireActivity()).get(BasicSettingsViewModel.class);
        final FallAsleepSettingsViewModel fallAsleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);

        basicSettingsViewModel.init(alarmTile.getBasicSettings());
        fallAsleepSettingsViewModel.init(alarmTile.getFallAsleepSettings());
        sleepSettingsViewModel.init(alarmTile.getSleepSettings());
    }

    private void resetViewModels() {
        final BasicSettingsViewModel basicSettingsViewModel = ViewModelProviders.of(requireActivity()).get(BasicSettingsViewModel.class);
        final FallAsleepSettingsViewModel fallAsleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);

        basicSettingsViewModel.reset();
        fallAsleepSettingsViewModel.reset();
        sleepSettingsViewModel.reset();
    }

    @Override
    public void onDestroyView() {
        //db.settingsDao().update(settings);
        super.onDestroyView();
    }
}
