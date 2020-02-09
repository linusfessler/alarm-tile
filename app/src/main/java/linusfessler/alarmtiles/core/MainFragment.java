package linusfessler.alarmtiles.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.alarm.AlarmViewModel;
import linusfessler.alarmtiles.alarm.AlarmViewModelFactory;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.shared.VibrationManager;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModel;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModelFactory;
import linusfessler.alarmtiles.timer.TimerViewModel;
import linusfessler.alarmtiles.timer.TimerViewModelFactory;

import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.toggle;

public class MainFragment extends Fragment {

    @Inject
    SleepTimerViewModel sleepTimerViewModel;

    @Inject
    AlarmViewModelFactory alarmViewModelFactory;

    @Inject
    TimerViewModelFactory timerViewModelFactory;

    @Inject
    StopwatchViewModelFactory stopwatchViewModelFactory;

    @Inject
    VibrationManager vibrationManager;

    private AlarmViewModel alarmViewModel;
    private TimerViewModel timerViewModel;
    private StopwatchViewModel stopwatchViewModel;

    private NavController navController;
    private FragmentMainBinding binding;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) requireActivity()
                .getApplicationContext())
                .getAppComponent()
                .inject(this);

        alarmViewModel = new ViewModelProvider(this, alarmViewModelFactory).get(AlarmViewModel.class);
        timerViewModel = new ViewModelProvider(this, timerViewModelFactory).get(TimerViewModel.class);
        stopwatchViewModel = new ViewModelProvider(this, stopwatchViewModelFactory).get(StopwatchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        initSleepTimer();
        initAlarm();
        initTimer();
        initStopwatch();

        return binding.getRoot();
    }

    private void initSleepTimer() {
        binding.alarmTiles.sleepTimer.setOnClickListener(view -> sleepTimerViewModel.dispatch(toggle()));
        binding.alarmTiles.sleepTimer.setOnLongClickListener(view -> {
            vibrationManager.vibrate();
            navController.navigate(MainFragmentDirections.actionMainFragmentToSleepTimerConfigFragment());
            return true;
        });

        disposable.add(sleepTimerViewModel.getSleepTimer()
                .subscribe(sleepTimer -> binding.alarmTiles.sleepTimer.setEnabled(sleepTimer.isEnabled())));

        disposable.add(sleepTimerViewModel.getTimeLeft()
                .subscribe(binding.alarmTiles.sleepTimer::setSubtitle));
    }

    private void initAlarm() {
        alarmViewModel.getAlarm().observe(getViewLifecycleOwner(), alarm -> {
            if (alarm == null) {
                return;
            }
            binding.alarmTiles.alarm.setEnabled(alarm.isEnabled());
            binding.alarmTiles.alarm.setOnClickListener(view -> alarmViewModel.toggle(alarm));
        });

        alarmViewModel.getTileLabel().observe(getViewLifecycleOwner(), binding.alarmTiles.alarm::setLabel);
    }

    private void initTimer() {
        timerViewModel.getTimer().observe(getViewLifecycleOwner(), timer -> {
            if (timer == null) {
                return;
            }
            binding.alarmTiles.timer.setEnabled(timer.isEnabled());
            binding.alarmTiles.timer.setOnClickListener(view -> timerViewModel.toggle(timer));
        });

        timerViewModel.getTileLabel().observe(getViewLifecycleOwner(), binding.alarmTiles.timer::setLabel);
    }

    private void initStopwatch() {
        binding.alarmTiles.stopwatch.setOnClickListener(view -> stopwatchViewModel.onClick());

        disposable.add(stopwatchViewModel.isEnabled()
                .subscribe(binding.alarmTiles.stopwatch::setEnabled));

        disposable.add(stopwatchViewModel.getElapsedTime()
                .subscribe(binding.alarmTiles.stopwatch::setSubtitle));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        binding.alarmTiles.sleepTimer.setOnClickListener(null);
        binding.alarmTiles.sleepTimer.setOnLongClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
