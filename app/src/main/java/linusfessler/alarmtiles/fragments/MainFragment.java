package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.alarm.AlarmViewModel;
import linusfessler.alarmtiles.alarm.AlarmViewModelFactory;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.sleeptimer.SleepTimerState;
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

    @Inject
    InputMethodManager inputMethodManager;

    private SleepTimerViewModel sleepTimerViewModel;
    private AlarmViewModel alarmViewModel;
    private TimerViewModel timerViewModel;
    private StopwatchViewModel stopwatchViewModel;

    private NavController navController;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) this.requireActivity().getApplicationContext()).getAppComponent().inject(this);

        this.sleepTimerViewModel = new ViewModelProvider(this, this.sleepTimerViewModelFactory).get(SleepTimerViewModel.class);
        this.alarmViewModel = new ViewModelProvider(this, this.alarmViewModelFactory).get(AlarmViewModel.class);
        this.timerViewModel = new ViewModelProvider(this, this.timerViewModelFactory).get(TimerViewModel.class);
        this.stopwatchViewModel = new ViewModelProvider(this, this.stopwatchViewModelFactory).get(StopwatchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.navController = NavHostFragment.findNavController(this);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        this.initSleepTimer(binding);
        this.initAlarm(binding);
        this.initTimer(binding);
        this.initStopwatch(binding);

        return binding.getRoot();
    }

    private void initSleepTimer(final FragmentMainBinding binding) {
        binding.alarmTiles.sleepTimer.setOnClickListener(view -> this.sleepTimerViewModel.onClick());
        binding.alarmTiles.sleepTimer.setOnLongClickListener(view -> {
            this.navController.navigate(MainFragmentDirections.actionMainFragmentToSleepTimerConfigFragment());
            return true;
        });

        this.disposable.add(this.sleepTimerViewModel.getSleepTimer()
                .subscribe(sleepTimer -> binding.alarmTiles.sleepTimer.setEnabled(sleepTimer.getState() == SleepTimerState.RUNNING)));

        this.disposable.add(this.sleepTimerViewModel.getTimeLeft()
                .subscribe(binding.alarmTiles.sleepTimer::setSubtitle));
    }

    private void initAlarm(final FragmentMainBinding binding) {
        this.alarmViewModel.getAlarm().observe(this.getViewLifecycleOwner(), alarm -> {
            if (alarm == null) {
                return;
            }
            binding.alarmTiles.alarm.setEnabled(alarm.isEnabled());
            binding.alarmTiles.alarm.setOnClickListener(view -> this.alarmViewModel.toggle(alarm));
        });

        this.alarmViewModel.getTileLabel().observe(this.getViewLifecycleOwner(), binding.alarmTiles.alarm::setLabel);
    }

    private void initTimer(final FragmentMainBinding binding) {
        this.timerViewModel.getTimer().observe(this.getViewLifecycleOwner(), timer -> {
            if (timer == null) {
                return;
            }
            binding.alarmTiles.timer.setEnabled(timer.isEnabled());
            binding.alarmTiles.timer.setOnClickListener(view -> this.timerViewModel.toggle(timer));
        });

        this.timerViewModel.getTileLabel().observe(this.getViewLifecycleOwner(), binding.alarmTiles.timer::setLabel);
    }

    private void initStopwatch(final FragmentMainBinding binding) {
        binding.alarmTiles.stopwatch.setOnClickListener(view -> this.stopwatchViewModel.onClick());
        // TODO: Vibrate

        this.disposable.add(this.stopwatchViewModel.isEnabled()
                .subscribe(binding.alarmTiles.stopwatch::setEnabled));

        this.disposable.add(this.stopwatchViewModel.getElapsedTime()
                .subscribe(binding.alarmTiles.stopwatch::setSubtitle));
    }

    @Override
    public void onDestroyView() {
        this.disposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
