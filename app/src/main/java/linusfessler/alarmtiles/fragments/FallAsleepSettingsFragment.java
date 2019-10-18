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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentFallAsleepSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.FallAsleepSettings;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;

public class FallAsleepSettingsFragment extends Fragment {

    private AlarmTile alarmTile;
    private FallAsleepSettingsViewModel viewModel;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = FallAsleepSettingsFragmentArgs.fromBundle(requireArguments()).getAlarmTile();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initViewModel();
        final FragmentFallAsleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fall_asleep_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        initNextButton(view, navController);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(FallAsleepSettingsViewModel.class);

        final FallAsleepSettings fallAsleepSettings = alarmTile.getFallAsleepSettings();
        if (fallAsleepSettings != null) {
            viewModel.setTimerEnabled(fallAsleepSettings.isTimerEnabled());
            viewModel.setSlowlyFadingMusicOut(fallAsleepSettings.isSlowlyFadingMusicOut());
        }
    }

    private void initNextButton(final View root, final NavController navController) {
        final MaterialButton button = root.findViewById(R.id.next_button);
        button.setOnClickListener(v -> {
            final FallAsleepSettings fallAsleepSettings = FallAsleepSettings.builder()
                    .slowlyFadingMusicOut(viewModel.isSlowlyFadingMusicOut())
                    .timerEnabled(viewModel.isTimerEnabled())
                    .build();
            alarmTile.setFallAsleepSettings(fallAsleepSettings);

            navController.navigate(FallAsleepSettingsFragmentDirections.actionFallAsleepSettingsFragmentToSleepSettingsFragment(alarmTile));
        });
    }

}
