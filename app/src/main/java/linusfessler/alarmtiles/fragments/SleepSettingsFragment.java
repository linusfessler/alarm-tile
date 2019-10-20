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

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSleepSettingsBinding;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;

public class SleepSettingsFragment extends Fragment implements TimePicker.OnTimeChangedListener {

    private SleepSettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SleepSettingsViewModel.class);
        final FragmentSleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_settings, container, false);
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
        final int hours = viewModel.getTimerHours();
        final int minutes = viewModel.getTimerMinutes();
        final DigitalTimePickerDialog timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);

        final LinearLayout timerDuration = root.findViewById(R.id.snooze_duration);
        timerDuration.setOnClickListener(v -> timePickerDialog.show());
    }

    private void initNextButton(final View root) {
        final MaterialButton button = root.findViewById(R.id.next_button);
        final NavDirections directions = SleepSettingsFragmentDirections.actionSleepSettingsFragmentToWakeUpSettingsFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int timerHours, final int timerMinutes) {
        viewModel.setTimerHours(timerHours);
        viewModel.setTimerMinutes(timerMinutes);
    }

}
