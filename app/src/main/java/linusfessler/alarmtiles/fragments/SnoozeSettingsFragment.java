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
import linusfessler.alarmtiles.databinding.FragmentSnoozeSettingsBinding;
import linusfessler.alarmtiles.viewmodel.SnoozeSettingsViewModel;

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
        final MaterialButton button = root.findViewById(R.id.next_button);
        final NavDirections directions = SnoozeSettingsFragmentDirections.actionSnoozeSettingsFragmentToAdvancedSettingsFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int snoozeHours, final int snoozeMinutes) {
        viewModel.setSnoozeHours(snoozeHours);
        viewModel.setSnoozeMinutes(snoozeMinutes);
    }

}
