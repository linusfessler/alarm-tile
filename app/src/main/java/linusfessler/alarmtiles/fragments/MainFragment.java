package linusfessler.alarmtiles.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        binding.soundSettings.setOnClickListener(v -> {
            final Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
            this.startActivity(intent);
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.sleep_timer_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sleepTimerModes.setAdapter(adapter);

        final SleepTimerViewModel sleepTimerViewModel = ViewModelProviders.of(this).get(SleepTimerViewModel.class);
        sleepTimerViewModel.getSleepTimer().observe(this, sleepTimer -> {
            if (sleepTimer == null) {
                return;
            }
            binding.sleepTimer.setEnabled(sleepTimer.isEnabled());
            binding.sleepTimer.setOnClickListener(v -> sleepTimerViewModel.toggleSleepTimer(sleepTimer));
        });
        sleepTimerViewModel.getTileLabel().observe(this, binding.sleepTimer::setLabel);

        return binding.getRoot();
    }
}
