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
import linusfessler.alarmtiles.alarm.AlarmViewModel;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModel;
import linusfessler.alarmtiles.timer.TimerViewModel;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        this.sleepTimer(binding);
        this.alarm(binding);
        this.timer(binding);
        this.stopwatch(binding);

        binding.soundSettings.setOnClickListener(v -> {
            final Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
            this.startActivity(intent);
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.sleep_timer_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sleepTimerModes.setAdapter(adapter);

        return binding.getRoot();
    }

    private void sleepTimer(final FragmentMainBinding binding) {
        final SleepTimerViewModel sleepTimerViewModel = ViewModelProviders.of(this).get(SleepTimerViewModel.class);
        sleepTimerViewModel.getSleepTimer().observe(this, sleepTimer -> {
            if (sleepTimer == null) {
                return;
            }
            binding.alarmTiles.sleepTimer.setEnabled(sleepTimer.isEnabled());
            binding.alarmTiles.sleepTimer.setOnClickListener(v -> sleepTimerViewModel.toggle(sleepTimer));
        });
        sleepTimerViewModel.getTileLabel().observe(this, binding.alarmTiles.sleepTimer::setLabel);
    }

    private void alarm(final FragmentMainBinding binding) {
        final AlarmViewModel alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        alarmViewModel.getAlarm().observe(this, alarm -> {
            if (alarm == null) {
                return;
            }
            binding.alarmTiles.alarm.setEnabled(alarm.isEnabled());
            binding.alarmTiles.alarm.setOnClickListener(v -> alarmViewModel.toggle(alarm));
        });
        alarmViewModel.getTileLabel().observe(this, binding.alarmTiles.alarm::setLabel);
    }

    private void timer(final FragmentMainBinding binding) {
        final TimerViewModel timerViewModel = ViewModelProviders.of(this).get(TimerViewModel.class);
        timerViewModel.getTimer().observe(this, timer -> {
            if (timer == null) {
                return;
            }
            binding.alarmTiles.timer.setEnabled(timer.isEnabled());
            binding.alarmTiles.timer.setOnClickListener(v -> timerViewModel.toggle(timer));
        });
        timerViewModel.getTileLabel().observe(this, binding.alarmTiles.timer::setLabel);
    }

    private void stopwatch(final FragmentMainBinding binding) {
        final StopwatchViewModel stopwatchViewModel = ViewModelProviders.of(this).get(StopwatchViewModel.class);
        stopwatchViewModel.getStopwatch().observe(this, stopwatch -> {
            if (stopwatch == null) {
                return;
            }
            binding.alarmTiles.stopwatch.setEnabled(stopwatch.isEnabled());
            binding.alarmTiles.stopwatch.setOnClickListener(v -> stopwatchViewModel.toggle(stopwatch));
        });
        stopwatchViewModel.getTileLabel().observe(this, binding.alarmTiles.stopwatch::setLabel);
    }
}
