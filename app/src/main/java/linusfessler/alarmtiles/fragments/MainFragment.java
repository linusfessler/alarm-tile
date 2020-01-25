package linusfessler.alarmtiles.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.alarm.AlarmViewModel;
import linusfessler.alarmtiles.alarm.AlarmViewModelFactory;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.dialogs.TimeInputDialog;
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

    private TimeInputDialog timeInputDialog;

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

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        this.initSleepTimer(binding);
        this.initAlarm(binding);
        this.initTimer(binding);
        this.initStopwatch(binding);

        return binding.getRoot();
    }

    private void initSleepTimer(final FragmentMainBinding binding) {
        this.timeInputDialog = new TimeInputDialog(this.requireContext());
        this.timeInputDialog.setTitle(R.string.sleep_timer_dialog_title);
        this.timeInputDialog.setButton(DialogInterface.BUTTON_NEGATIVE, this.getString(R.string.dialog_cancel), (dialog, which) -> {
        });

        this.disposable.add(this.sleepTimerViewModel.getSleepTimer().subscribe(sleepTimer -> {
            if (sleepTimer.isEnabled()) {
                // Special case: dismiss the dialog if sleep timer was enabled through the quick settings in the mean time
                this.timeInputDialog.dismiss();
            }

            binding.alarmTiles.sleepTimer.setEnabled(sleepTimer.isEnabled());
            binding.alarmTiles.sleepTimer.setOnClickListener(view -> {
                if (sleepTimer.isEnabled()) {
                    this.sleepTimerViewModel.cancel();
                } else {
                    this.timeInputDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.dialog_ok), (dialog, which) -> {
                        final long duration = this.timeInputDialog.getMillis();
                        this.sleepTimerViewModel.start(duration);
                    });

                    this.timeInputDialog.clear(TimeUnit.MINUTES);

                    this.timeInputDialog.show();
                }
            });
        }));

        this.disposable.add(this.sleepTimerViewModel.getTimeLeft().subscribe(binding.alarmTiles.sleepTimer::setSubtitle));
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
        this.stopwatchViewModel.getStopwatch().observe(this.getViewLifecycleOwner(), stopwatch -> {
            if (stopwatch == null) {
                return;
            }
            binding.alarmTiles.stopwatch.setEnabled(stopwatch.isEnabled());
            binding.alarmTiles.stopwatch.setOnClickListener(view -> this.stopwatchViewModel.toggle(stopwatch));
        });

        this.stopwatchViewModel.getTileLabel().observe(this.getViewLifecycleOwner(), binding.alarmTiles.stopwatch::setLabel);
    }

    @Override
    public void onDestroyView() {
        this.timeInputDialog.dismiss();
        this.disposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
