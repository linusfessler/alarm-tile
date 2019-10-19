package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentFallAsleepSettingsBinding;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;

public class FallAsleepSettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FallAsleepSettingsViewModel viewModel = ViewModelProviders.of(requireActivity()).get(FallAsleepSettingsViewModel.class);
        final FragmentFallAsleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fall_asleep_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNextButton(view);
    }

    private void initNextButton(final View root) {
        final MaterialButton button = root.findViewById(R.id.next_button);
        final NavDirections directions = FallAsleepSettingsFragmentDirections.actionFallAsleepSettingsFragmentToSleepSettingsFragment();
        button.setOnClickListener(Navigation.createNavigateOnClickListener(directions));
    }

}
