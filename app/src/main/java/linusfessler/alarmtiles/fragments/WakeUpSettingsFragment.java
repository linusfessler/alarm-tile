package linusfessler.alarmtiles.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentWakeUpSettingsBinding;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;
import linusfessler.alarmtiles.viewmodel.WakeUpSettingsViewModel;

public class WakeUpSettingsFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private WakeUpSettingsViewModel viewModel;
    private boolean is24Hours;

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
        initNextButton(view);
    }

    private void initTimePicker(final View root) {
        final Context context = requireContext();
        final int alarmHour = viewModel.getAlarmHour();
        final int alarmMinute = viewModel.getAlarmMinute();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, alarmHour, alarmMinute, is24Hours);

        final LinearLayout alarmTime = root.findViewById(R.id.alarm_time);
        alarmTime.setOnClickListener(v -> timePickerDialog.show());
    }

    private void initNextButton(final View root) {
        final MaterialButton button = root.findViewById(R.id.next_button);
        final NavDirections directions = WakeUpSettingsFragmentDirections.actionWakeUpSettingsFragmentToSnoozeSettingsFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onTimeSet(final TimePicker view, final int alarmHour, final int alarmMinute) {
        viewModel.setAlarmHour(alarmHour);
        viewModel.setAlarmMinute(alarmMinute);
    }
}
