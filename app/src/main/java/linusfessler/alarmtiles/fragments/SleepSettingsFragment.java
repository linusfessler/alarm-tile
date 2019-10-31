package linusfessler.alarmtiles.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSleepSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;

public class SleepSettingsFragment extends Fragment {

    private static final String ALARM_TILE_ARG_NAME = "alarm_tile";
    private static final String ACTION_ZEN_MODE_PRIORITY_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";

    private AlarmTile alarmTile;

    public static SleepSettingsFragment newInstance(final AlarmTile alarmTile) {
        final SleepSettingsFragment fragment = new SleepSettingsFragment();

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

        final SleepSettingsViewModel viewModel = ViewModelProviders.of(this).get(SleepSettingsViewModel.class);
        viewModel.setAlarmTile(alarmTile);
        final FragmentSleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initOpenSettingsButton(view);
    }

    private void initOpenSettingsButton(final View root) {
        final MaterialButton openSettingsButton = root.findViewById(R.id.open_settings_button);
        openSettingsButton.setOnClickListener(v -> {
            final Intent intent = new Intent(ACTION_ZEN_MODE_PRIORITY_SETTINGS);
            startActivity(intent);
        });
    }

}
