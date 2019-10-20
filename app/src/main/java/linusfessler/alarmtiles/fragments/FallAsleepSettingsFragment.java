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
import linusfessler.alarmtiles.databinding.FragmentFallAsleepSettingsBinding;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;

public class FallAsleepSettingsFragment extends Fragment implements TimePicker.OnTimeChangedListener {

    private FallAsleepSettingsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final FragmentFallAsleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fall_asleep_settings, container, false);
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
        final int hours = viewModel.getHours();
        final int minutes = viewModel.getMinutes();
        final DigitalTimePickerDialog timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);

        final LinearLayout duration = root.findViewById(R.id.duration);
        duration.setOnClickListener(v -> timePickerDialog.show());
    }

    private void initNextButton(final View root) {
        final MaterialButton button = root.findViewById(R.id.next_button);
        final NavDirections directions = FallAsleepSettingsFragmentDirections.actionFallAsleepSettingsFragmentToSleepSettingsFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int hours, final int minute) {
        viewModel.setHours(hours);
        viewModel.setMinutes(minute);
    }
}
