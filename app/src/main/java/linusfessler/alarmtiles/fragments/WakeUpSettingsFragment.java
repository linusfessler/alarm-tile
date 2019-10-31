package linusfessler.alarmtiles.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentWakeUpSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.WakeUpSettingsViewModel;

public class WakeUpSettingsFragment extends Fragment {

    private static final String ALARM_TILE_ARG_NAME = "alarm_tile";

    private AlarmTile alarmTile;
    private WakeUpSettingsViewModel viewModel;
    private boolean is24Hours;
    private TimePickerDialog alarmTimePickerDialog;
    private DigitalTimePickerDialog timerTimePickerDialog;
    private DigitalTimePickerDialog snoozeTimePickerDialog;
    private DigitalTimePickerDialog volumeTimerTimePickerDialog;
    private DigitalTimePickerDialog dismissTimerTimePickerDialog;

    public static WakeUpSettingsFragment newInstance(final AlarmTile alarmTile) {
        final WakeUpSettingsFragment fragment = new WakeUpSettingsFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ALARM_TILE_ARG_NAME, alarmTile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = (AlarmTile) requireArguments().get(ALARM_TILE_ARG_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentActivity activity = requireActivity();

        viewModel = ViewModelProviders.of(this).get(WakeUpSettingsViewModel.class);
        viewModel.setAlarmTile(alarmTile);
        final FragmentWakeUpSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wake_up_settings, container, false);
        binding.setViewModel(viewModel);

        is24Hours = DateFormat.is24HourFormat(activity);
        viewModel.set24Hours(is24Hours);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAlarmTimePicker(view);
        initTimerTimePicker(view);
        initSnoozeTimePicker(view);
        initVolumeTimerTimePicker(view);
        initDismissTimerTimePicker(view);
    }

    private void initAlarmTimePicker(final View root) {
        final Context context = requireContext();
        final int alarmHour = viewModel.getAlarmHour();
        final int alarmMinute = viewModel.getAlarmMinute();
        final TimePickerDialog.OnTimeSetListener listener = (view, newAlarmHour, newAlarmMinute) -> {
            viewModel.setAlarmHour(newAlarmHour);
            viewModel.setAlarmMinute(newAlarmMinute);
        };

        alarmTimePickerDialog = new TimePickerDialog(context, listener, alarmHour, alarmMinute, is24Hours);

        final MaterialTextView alarmTime = root.findViewById(R.id.alarm_time);
        alarmTime.setOnClickListener(v -> alarmTimePickerDialog.show());
    }

    private void initTimerTimePicker(final View root) {
        final Context context = requireContext();
        final int timerHours = viewModel.getTimerHours();
        final int timerMinutes = viewModel.getTimerMinutes();
        final TimePicker.OnTimeChangedListener listener = (view, newTimerHours, newTimerMinutes) -> {
            viewModel.setTimerHours(newTimerHours);
            viewModel.setTimerMinutes(newTimerMinutes);
        };

        timerTimePickerDialog = new DigitalTimePickerDialog(context, listener, timerHours, timerMinutes, true);

        final MaterialTextView timerDuration = root.findViewById(R.id.timer_duration);
        timerDuration.setOnClickListener(v -> timerTimePickerDialog.show());
    }

    private void initSnoozeTimePicker(final View root) {
        final Context context = requireContext();
        final int snoozeHours = viewModel.getSnoozeHours();
        final int snoozeMinutes = viewModel.getSnoozeMinutes();
        final TimePicker.OnTimeChangedListener listener = (view, newSnoozeHours, newSnoozeMinutes) -> {
            viewModel.setSnoozeHours(newSnoozeHours);
            viewModel.setSnoozeMinutes(newSnoozeMinutes);
        };

        snoozeTimePickerDialog = new DigitalTimePickerDialog(context, listener, snoozeHours, snoozeMinutes, true);

        final MaterialTextView snoozeDuration = root.findViewById(R.id.snooze_duration);
        snoozeDuration.setOnClickListener(v -> snoozeTimePickerDialog.show());
    }

    private void initVolumeTimerTimePicker(final View root) {
        final Context context = requireContext();
        final int volumeHours = viewModel.getVolumeTimerHours();
        final int volumeMinutes = viewModel.getVolumeTimerMinutes();
        final TimePicker.OnTimeChangedListener listener = (view, newVolumeHours, newVolumeMinutes) -> {
            viewModel.setVolumeTimerHours(newVolumeHours);
            viewModel.setVolumeTimerMinutes(newVolumeMinutes);
        };

        volumeTimerTimePickerDialog = new DigitalTimePickerDialog(context, listener, volumeHours, volumeMinutes, true);

        final MaterialTextView volumeTimerDuration = root.findViewById(R.id.volume_timer_duration);
        volumeTimerDuration.setOnClickListener(v -> volumeTimerTimePickerDialog.show());
    }

    private void initDismissTimerTimePicker(final View root) {
        final Context context = requireContext();
        final int timerHours = viewModel.getDismissTimerHours();
        final int timerMinutes = viewModel.getDismissTimerMinutes();
        final TimePicker.OnTimeChangedListener listener = (view, newDismissHours, newDismissMinutes) -> {
            viewModel.setDismissTimerHours(newDismissHours);
            viewModel.setDismissTimerMinutes(newDismissMinutes);
        };

        dismissTimerTimePickerDialog = new DigitalTimePickerDialog(context, listener, timerHours, timerMinutes, true);

        final MaterialTextView dismissTimerDuration = root.findViewById(R.id.dismiss_timer_duration);
        dismissTimerDuration.setOnClickListener(v -> dismissTimerTimePickerDialog.show());
    }

    @Override
    public void onDestroyView() {
        alarmTimePickerDialog.dismiss();
        timerTimePickerDialog.dismiss();
        snoozeTimePickerDialog.dismiss();
        volumeTimerTimePickerDialog.dismiss();
        dismissTimerTimePickerDialog.dismiss();
        super.onDestroyView();
    }
}
