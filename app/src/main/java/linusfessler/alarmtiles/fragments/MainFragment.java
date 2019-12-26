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

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.alarm.AlarmViewModel;
import linusfessler.alarmtiles.alarm.AlarmViewModelFactory;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModelFactory;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModel;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModelFactory;
import linusfessler.alarmtiles.timer.TimerViewModel;
import linusfessler.alarmtiles.timer.TimerViewModelFactory;

public class MainFragment extends Fragment {

    @Inject
    SleepTimerViewModelFactory sleepTimerViewModelFactory;

    @Inject
    AlarmViewModelFactory alarmViewModelFactory;

    @Inject
    TimerViewModelFactory timerViewModelFactory;

    @Inject
    StopwatchViewModelFactory stopwatchViewModelFactory;

    private SleepTimerViewModel sleepTimerViewModel;
    private AlarmViewModel alarmViewModel;
    private TimerViewModel timerViewModel;
    private StopwatchViewModel stopwatchViewModel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) this.requireActivity().getApplicationContext()).getAppComponent().inject(this);

        this.sleepTimerViewModel = ViewModelProviders.of(this, this.sleepTimerViewModelFactory).get(SleepTimerViewModel.class);
        this.alarmViewModel = ViewModelProviders.of(this, this.alarmViewModelFactory).get(AlarmViewModel.class);
        this.timerViewModel = ViewModelProviders.of(this, this.timerViewModelFactory).get(TimerViewModel.class);
        this.stopwatchViewModel = ViewModelProviders.of(this, this.stopwatchViewModelFactory).get(StopwatchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        this.bindSleepTimer(binding);
        this.bindAlarm(binding);
        this.bindTimer(binding);
        this.bindStopwatch(binding);

        binding.soundSettings.setOnClickListener(v -> {
            final Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
            this.startActivity(intent);
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.sleep_timer_modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sleepTimerModes.setAdapter(adapter);

        return binding.getRoot();
    }

    private void bindSleepTimer(final FragmentMainBinding binding) {// TODO: Replace runOnUiThread by mainThread scheduler or so
        this.disposable.add(this.sleepTimerViewModel.getSleepTimer().subscribeOn(AndroidSchedulers.mainThread()).subscribe(sleepTimer -> {
            this.requireActivity().runOnUiThread(() -> {
                binding.alarmTiles.sleepTimer.setEnabled(sleepTimer.isEnabled());
                binding.alarmTiles.sleepTimer.setOnClickListener(v -> this.sleepTimerViewModel.onClick(sleepTimer));
            });
        }));

        this.disposable.add(this.sleepTimerViewModel.getTileLabel().subscribeOn(AndroidSchedulers.mainThread()).subscribe(tileLabel ->
                this.requireActivity().runOnUiThread(() ->
                        binding.alarmTiles.sleepTimer.setLabel(tileLabel))));
    }

    private void bindAlarm(final FragmentMainBinding binding) {
        this.alarmViewModel.getAlarm().observe(this, alarm -> {
            if (alarm == null) {
                return;
            }
            binding.alarmTiles.alarm.setEnabled(alarm.isEnabled());
            binding.alarmTiles.alarm.setOnClickListener(v -> this.alarmViewModel.toggle(alarm));
        });

        this.alarmViewModel.getTileLabel().observe(this, binding.alarmTiles.alarm::setLabel);
    }

    private void bindTimer(final FragmentMainBinding binding) {
        this.timerViewModel.getTimer().observe(this, timer -> {
            if (timer == null) {
                return;
            }
            binding.alarmTiles.timer.setEnabled(timer.isEnabled());
            binding.alarmTiles.timer.setOnClickListener(v -> this.timerViewModel.toggle(timer));
        });

        this.timerViewModel.getTileLabel().observe(this, binding.alarmTiles.timer::setLabel);
    }

    private void bindStopwatch(final FragmentMainBinding binding) {
        this.stopwatchViewModel.getStopwatch().observe(this, stopwatch -> {
            if (stopwatch == null) {
                return;
            }
            binding.alarmTiles.stopwatch.setEnabled(stopwatch.isEnabled());
            binding.alarmTiles.stopwatch.setOnClickListener(v -> this.stopwatchViewModel.toggle(stopwatch));
        });

        this.stopwatchViewModel.getTileLabel().observe(this, binding.alarmTiles.stopwatch::setLabel);
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
