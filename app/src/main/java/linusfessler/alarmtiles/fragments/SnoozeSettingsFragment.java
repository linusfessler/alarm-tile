package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executors;

import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSnoozeSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.model.GeneralSettings;
import linusfessler.alarmtiles.model.SleepSettings;
import linusfessler.alarmtiles.model.SnoozeSettings;
import linusfessler.alarmtiles.model.WakeUpSettings;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.GeneralSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.SnoozeSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.WakeUpSettingsViewModel;

public class SnoozeSettingsFragment extends Fragment implements TimePicker.OnTimeChangedListener {

    private SnoozeSettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SnoozeSettingsViewModel.class);
        final FragmentSnoozeSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_snooze_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTimePicker(view);
        initNextButton(view);
    }

    private void initTimePicker(final View root) {
        final Context context = requireContext();
        final int hours = viewModel.getSnoozeHours();
        final int minutes = viewModel.getSnoozeMinutes();
        final DigitalTimePickerDialog timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);

        final LinearLayout snoozeDuration = root.findViewById(R.id.snooze_duration);
        snoozeDuration.setOnClickListener(v -> timePickerDialog.show());
    }

    private void initNextButton(final View root) {
        final GeneralSettingsViewModel generalSettingsViewModel = ViewModelProviders.of(requireActivity()).get(GeneralSettingsViewModel.class);
        final FallAsleepSettingsViewModel fallAsleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);
        final WakeUpSettingsViewModel wakeUpSettingsViewModel = ViewModelProviders.of(requireActivity()).get(WakeUpSettingsViewModel.class);
        final SnoozeSettingsViewModel snoozeSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SnoozeSettingsViewModel.class);

        final GeneralSettings generalSettings = GeneralSettings.builder()
                .name(generalSettingsViewModel.getName())
                .iconResourceId(generalSettingsViewModel.getIconResourceId())
                .showingNotification(generalSettingsViewModel.isShowingNotification())
                .graduallyIncreasingVolume(generalSettingsViewModel.isGraduallyIncreasingVolume())
                .vibrating(generalSettingsViewModel.isVibrating())
                .turningOnFlashlight(generalSettingsViewModel.isTurningOnFlashlight())
                .build();

        final FallAsleepSettings fallAsleepSettings = FallAsleepSettings.builder()
                .timerEnabled(fallAsleepSettingsViewModel.isTimerEnabled())
                .timerHours(fallAsleepSettingsViewModel.getTimerHours())
                .timerMinutes(fallAsleepSettingsViewModel.getTimerMinutes())
                .slowlyFadingMusicOut(fallAsleepSettingsViewModel.isSlowlyFadingMusicOut())
                .build();

        final SleepSettings sleepSettings = SleepSettings.builder()
                .timerEnabled(sleepSettingsViewModel.isTimerEnabled())
                .timerHours(sleepSettingsViewModel.getTimerHours())
                .timerMinutes(sleepSettingsViewModel.getTimerMinutes())
                .enteringDoNotDisturb(sleepSettingsViewModel.isEnteringDoNotDisturb())
                .allowingPriorityNotifications(sleepSettingsViewModel.isAllowingPriorityNotifications())
                .build();

        final WakeUpSettings wakeUpSettings = WakeUpSettings.builder()
                .alarmEnabled(wakeUpSettingsViewModel.isAlarmEnabled())
                .alarmHour(wakeUpSettingsViewModel.getAlarmHour())
                .alarmMinute(wakeUpSettingsViewModel.getAlarmMinute())
                .build();

        final SnoozeSettings snoozeSettings = SnoozeSettings.builder()
                .snoozeEnabled(snoozeSettingsViewModel.isSnoozeEnabled())
                .snoozeHours(snoozeSettingsViewModel.getSnoozeHours())
                .snoozeMinutes(snoozeSettingsViewModel.getSnoozeMinutes())
                .build();

        final AlarmTile alarmTile = new AlarmTile(generalSettings, fallAsleepSettings, sleepSettings, wakeUpSettings, snoozeSettings);

        Executors.newSingleThreadExecutor().submit(() ->
                AppDatabase.getInstance(requireContext()).alarmTiles().insert(alarmTile));

        final MaterialButton button = root.findViewById(R.id.next_button);
        final NavDirections directions = SnoozeSettingsFragmentDirections.actionSnoozeSettingsFragmentToMainFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int snoozeHours, final int snoozeMinutes) {
        viewModel.setSnoozeHours(snoozeHours);
        viewModel.setSnoozeMinutes(snoozeMinutes);
    }

}
