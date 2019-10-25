package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSleepSettingsBinding;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;

public class SleepSettingsFragment extends SettingsFragment implements TimePicker.OnTimeChangedListener {

    private SleepSettingsViewModel viewModel;

    @Override
    public int getTitleResourceId() {
        return R.string.sleep_settings_title;
    }

    @Nullable
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
    }

    private void initTimePicker(final View root) {
        final Context context = requireContext();
        final int hours = viewModel.getTimerHours();
        final int minutes = viewModel.getTimerMinutes();
        final DigitalTimePickerDialog timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);

        final MaterialTextView timerDuration = root.findViewById(R.id.timer_duration);
        timerDuration.setOnClickListener(v -> timePickerDialog.show());
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int timerHours, final int timerMinutes) {
        viewModel.setTimerHours(timerHours);
        viewModel.setTimerMinutes(timerMinutes);
    }

}
