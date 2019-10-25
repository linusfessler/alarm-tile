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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentWakeUpSettingsBinding;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.WakeUpSettingsViewModel;

public class WakeUpSettingsFragment extends SettingsFragment implements TimePickerDialog.OnTimeSetListener {

    private WakeUpSettingsViewModel viewModel;
    private boolean is24Hours;

    @Override
    public int getTitleResourceId() {
        return R.string.wake_up_settings_title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentActivity activity = requireActivity();

        viewModel = ViewModelProviders.of(requireActivity()).get(WakeUpSettingsViewModel.class);
        final FragmentWakeUpSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wake_up_settings, container, false);
        binding.setViewModel(viewModel);

        final SleepSettingsViewModel sleepSettingsViewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);
        viewModel.setSleepSettingsTimerEnabled(sleepSettingsViewModel.isTimerEnabled());

        is24Hours = DateFormat.is24HourFormat(activity);
        viewModel.set24Hours(is24Hours);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTimePicker(view);
    }

    private void initTimePicker(final View root) {
        final Context context = requireContext();
        final int alarmHour = viewModel.getAlarmHour();
        final int alarmMinute = viewModel.getAlarmMinute();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, alarmHour, alarmMinute, is24Hours);

        final MaterialTextView alarmTime = root.findViewById(R.id.alarm_time);
        alarmTime.setOnClickListener(v -> timePickerDialog.show());
    }

    @Override
    public void onTimeSet(final TimePicker view, final int alarmHour, final int alarmMinute) {
        viewModel.setAlarmHour(alarmHour);
        viewModel.setAlarmMinute(alarmMinute);
    }
}
