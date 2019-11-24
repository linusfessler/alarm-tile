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

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        final SleepTimerViewModel sleepTimerViewModel = ViewModelProviders.of(this).get(SleepTimerViewModel.class);
        sleepTimerViewModel.getSleepTimer().observe(this, sleepTimer -> {
            binding.sleepTimer.setEnabled(sleepTimer.isEnabled());
            binding.sleepTimer.setOnClickListener(v -> {
                sleepTimer.setEnabled(!sleepTimer.isEnabled());
                sleepTimerViewModel.setSleepTimer(sleepTimer);
            });
        });

        return binding.getRoot();
    }
}
