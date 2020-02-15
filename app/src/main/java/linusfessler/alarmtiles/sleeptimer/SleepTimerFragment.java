package linusfessler.alarmtiles.sleeptimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.databinding.FragmentSleepTimerBinding;
import linusfessler.alarmtiles.shared.VibrationManager;

public class SleepTimerFragment extends Fragment {

    @Inject
    SleepTimerViewModel viewModel;

    @Inject
    InputMethodManager inputMethodManager;

    @Inject
    VibrationManager vibrationManager;

    private FragmentSleepTimerBinding binding;
    private SleepTimerStartDialog startDialog;
    private AlertDialog descriptionDialog;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) requireActivity()
                .getApplicationContext())
                .getAppComponent()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_timer, container, false);
        startDialog = new SleepTimerStartDialog(requireContext(), inputMethodManager, viewModel);
        descriptionDialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.sleep_timer)
                .setMessage(R.string.sleep_timer_description)
                .setCancelable(true)
                .create();

        binding.sleepTimer.setOnClickListener(view ->
                disposable.add(viewModel.getSleepTimer()
                        .firstElement()
                        .subscribe(sleepTimer -> {
                            if (sleepTimer.isEnabled()) {
                                viewModel.dispatch(new SleepTimerEvent.Cancel());
                            } else {
                                startDialog.show();
                            }
                        })));

        binding.sleepTimer.setOnLongClickListener(view -> {
            vibrationManager.vibrate();
            descriptionDialog.show();
            return true;
        });

        disposable.add(viewModel.getSleepTimer()
                .subscribe(sleepTimer -> {
                    // It's possible that the sleep timer is enabled through the quick settings while the start dialog is shown, dismiss it in this case
                    if (sleepTimer.isEnabled()) {
                        startDialog.dismiss();
                    }
                    binding.sleepTimer.setEnabled(sleepTimer.isEnabled());
                }));

        disposable.add(viewModel.getTimeLeft()
                .subscribe(binding.sleepTimer::setSubtitle));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        binding.sleepTimer.setOnClickListener(null);
        binding.sleepTimer.setOnLongClickListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        startDialog.dismiss();
        descriptionDialog.dismiss();
    }
}
