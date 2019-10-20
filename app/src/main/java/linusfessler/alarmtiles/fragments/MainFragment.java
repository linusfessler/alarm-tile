package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import linusfessler.alarmtiles.AlarmTileListAdapter;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.BuildConfig;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.Settings;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.BasicSettings;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.model.SnoozeSettings;
import linusfessler.alarmtiles.model.WakeUpSettings;
import linusfessler.alarmtiles.viewmodel.BasicSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SnoozeSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.WakeUpSettingsViewModel;

public class MainFragment extends Fragment {

    private static final String LAST_VERSION_CODE = "last_version_code";

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

        final List<AlarmTile> alarmTiles = getAlarmTiles(requireContext());

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

    private List<AlarmTile> getAlarmTiles(final Context context) {
        //if (isFirstStart(context)) {
        return createAlarmTiles();
        //}
        // TODO: Get from DB
    }

    private List<AlarmTile> createAlarmTiles() {
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

        final FallAsleepSettings workweekFallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(0)
                .slowlyFadingMusicOut(true)
                .build();
        final FallAsleepSettings weekendFallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(0)
                .slowlyFadingMusicOut(true)
                .build();
        final FallAsleepSettings napFallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(0)
                .slowlyFadingMusicOut(true)
                .build();

        workweekAlarmTile.setFallAsleepSettings(workweekFallAsleepSettings);
        weekendTimerTile.setFallAsleepSettings(weekendFallAsleepSettings);
        napTile.setFallAsleepSettings(napFallAsleepSettings);

        final SleepSettings workweekSleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();
        final SleepSettings weekendSleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();
        final SleepSettings napSleepSettings = SleepSettings.builder()
                .timerEnabled(true)
                .timerHours(0)
                .timerMinutes(0)
                .enteringDoNotDisturb(true)
                .allowingPriorityNotifications(true)
                .build();

        workweekAlarmTile.setSleepSettings(workweekSleepSettings);
        weekendTimerTile.setSleepSettings(weekendSleepSettings);
        napTile.setSleepSettings(napSleepSettings);

        final WakeUpSettings workweekWakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(true)
                .alarmHour(6)
                .alarmMinute(30)
                .build();
        final WakeUpSettings weekendWakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(false)
                .build();
        final WakeUpSettings napWakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(false)
                .build();

        workweekAlarmTile.setWakeUpSettings(workweekWakeUpSettings);
        weekendTimerTile.setWakeUpSettings(weekendWakeUpSettings);
        napTile.setWakeUpSettings(napWakeUpSettings);

        final SnoozeSettings workweekSnoozeSettings = SnoozeSettings.builder()
                .snoozeEnabled(true)
                .snoozeHours(0)
                .snoozeMinutes(15)
                .build();
        final SnoozeSettings weekendSnoozeSettings = SnoozeSettings.builder()
                .snoozeEnabled(false)
                .build();
        final SnoozeSettings napSnoozeSettings = SnoozeSettings.builder()
                .snoozeEnabled(true)
                .snoozeHours(0)
                .snoozeMinutes(15)
                .build();

        workweekAlarmTile.setSnoozeSettings(workweekSnoozeSettings);
        weekendTimerTile.setSnoozeSettings(weekendSnoozeSettings);
        napTile.setSnoozeSettings(napSnoozeSettings);

        alarmTiles.add(workweekAlarmTile);
        alarmTiles.add(weekendTimerTile);
        alarmTiles.add(napTile);

        // TODO: Store in DB

        return alarmTiles;
    }

    public boolean isFirstStart(final Context context) {
        final int versionCode = BuildConfig.VERSION_CODE;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final int lastVersionCode = sharedPreferences.getInt(LAST_VERSION_CODE, -1);
        sharedPreferences.edit().putInt(LAST_VERSION_CODE, versionCode).apply();
        return versionCode > lastVersionCode;
    }

    private void initViewModels(final AlarmTile alarmTile) {
        final BasicSettingsViewModel basicSettingsViewModel = ViewModelProviders.of(requireActivity()).get(BasicSettingsViewModel.class);
        final FallAsleepSettingsViewModel fallAsleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);
        final WakeUpSettingsViewModel wakeUpSettingsViewModel = ViewModelProviders.of(requireActivity()).get(WakeUpSettingsViewModel.class);
        final SnoozeSettingsViewModel snoozeSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SnoozeSettingsViewModel.class);

        basicSettingsViewModel.init(alarmTile.getBasicSettings());
        fallAsleepSettingsViewModel.init(alarmTile.getFallAsleepSettings());
        sleepSettingsViewModel.init(alarmTile.getSleepSettings());
        wakeUpSettingsViewModel.init(alarmTile.getWakeUpSettings());
        snoozeSettingsViewModel.init(alarmTile.getSnoozeSettings());
    }

    private void resetViewModels() {
        final BasicSettingsViewModel basicSettingsViewModel = ViewModelProviders.of(requireActivity()).get(BasicSettingsViewModel.class);
        final FallAsleepSettingsViewModel fallAsleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);
        final WakeUpSettingsViewModel wakeUpSettingsViewModel = ViewModelProviders.of(requireActivity()).get(WakeUpSettingsViewModel.class);
        final SnoozeSettingsViewModel snoozeSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SnoozeSettingsViewModel.class);

        basicSettingsViewModel.reset();
        fallAsleepSettingsViewModel.reset();
        sleepSettingsViewModel.reset();
        wakeUpSettingsViewModel.reset();
        snoozeSettingsViewModel.reset();
    }

    @Override
    public void onDestroyView() {
        //db.settingsDao().update(settings);
        super.onDestroyView();
    }
}
