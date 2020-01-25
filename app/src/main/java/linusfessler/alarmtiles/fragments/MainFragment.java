package linusfessler.alarmtiles.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import linusfessler.alarmtiles.dialogs.TimeInputDialogBuilder;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModel;
import linusfessler.alarmtiles.sleeptimer.SleepTimerViewModelFactory;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModel;
import linusfessler.alarmtiles.stopwatch.StopwatchViewModelFactory;
import linusfessler.alarmtiles.timer.TimerViewModel;
import linusfessler.alarmtiles.timer.TimerViewModelFactory;
import linusfessler.alarmtiles.views.TimeInput;

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

    private AlertDialog sleepTimerAlertDialog;
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

        this.sleepTimerAlertDialog = new TimeInputDialogBuilder(this.requireContext())
                .setTitle(R.string.sleep_timer_dialog_title)
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.navController = NavHostFragment.findNavController(this);

        final FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        this.bindSleepTimer(binding);
        this.bindAlarm(binding);
        this.bindTimer(binding);
        this.bindStopwatch(binding);

        return binding.getRoot();
    }

    private void bindSleepTimer(final FragmentMainBinding binding) {
        this.disposable.add(this.sleepTimerViewModel.getSleepTimer().subscribe(sleepTimer -> {
            if (sleepTimer.isEnabled()) {
                // Dismiss the dialog in case the sleep timer was enabled through the quick settings in the mean time
                this.sleepTimerAlertDialog.dismiss();
            }

            binding.alarmTiles.sleepTimer.setEnabled(sleepTimer.isEnabled());
            binding.alarmTiles.sleepTimer.setOnClickListener(v -> {
                if (sleepTimer.isEnabled()) {
                    this.sleepTimerViewModel.cancel();
                } else {
                    this.sleepTimerAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, this.getString(R.string.dialog_ok), (dialog, which) -> {
                        final TimeInput timeInput = this.sleepTimerAlertDialog.findViewById(R.id.time_input);
                        if (timeInput == null) {
                            throw new IllegalArgumentException("A time input is required as the view of the alert dialog.");
                        }

                        timeInput.getMillis().firstElement().subscribe(millis -> this.sleepTimerViewModel.start(millis)).dispose();
                    });

                    this.sleepTimerAlertDialog.show();
                }
            });
        }));

        this.disposable.add(this.sleepTimerViewModel.getTimeLeft().subscribe(binding.alarmTiles.sleepTimer::setSubtitle));
    }

    private void bindAlarm(final FragmentMainBinding binding) {
        this.alarmViewModel.getAlarm().observe(this.getViewLifecycleOwner(), alarm -> {
            if (alarm == null) {
                return;
            }
            binding.alarmTiles.alarm.setEnabled(alarm.isEnabled());
            binding.alarmTiles.alarm.setOnClickListener(v -> this.alarmViewModel.toggle(alarm));
        });

        this.alarmViewModel.getTileLabel().observe(this.getViewLifecycleOwner(), binding.alarmTiles.alarm::setLabel);
    }

    private void bindTimer(final FragmentMainBinding binding) {
        this.timerViewModel.getTimer().observe(this.getViewLifecycleOwner(), timer -> {
            if (timer == null) {
                return;
            }
            binding.alarmTiles.timer.setEnabled(timer.isEnabled());
            binding.alarmTiles.timer.setOnClickListener(v -> this.timerViewModel.toggle(timer));
        });

        this.timerViewModel.getTileLabel().observe(this.getViewLifecycleOwner(), binding.alarmTiles.timer::setLabel);
    }

    private void bindStopwatch(final FragmentMainBinding binding) {
        this.stopwatchViewModel.getStopwatch().observe(this.getViewLifecycleOwner(), stopwatch -> {
            if (stopwatch == null) {
                return;
            }
            binding.alarmTiles.stopwatch.setEnabled(stopwatch.isEnabled());
            binding.alarmTiles.stopwatch.setOnClickListener(v -> this.stopwatchViewModel.toggle(stopwatch));
        });

        this.stopwatchViewModel.getTileLabel().observe(this.getViewLifecycleOwner(), binding.alarmTiles.stopwatch::setLabel);
    }

    @Override
    public void onDestroyView() {
        this.sleepTimerAlertDialog.dismiss();
        this.disposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
