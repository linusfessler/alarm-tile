package linusfessler.alarmtiles.sleeptimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.core.MainFragmentDirections;
import linusfessler.alarmtiles.databinding.FragmentSleepTimerBinding;
import linusfessler.alarmtiles.shared.VibrationManager;

import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.toggle;

public class SleepTimerFragment extends Fragment {

    @Inject
    SleepTimerViewModel viewModel;

    @Inject
    VibrationManager vibrationManager;

    private FragmentSleepTimerBinding binding;
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

        final NavController navController = NavHostFragment.findNavController(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_timer, container, false);

        binding.sleepTimer.setOnClickListener(view -> viewModel.dispatch(toggle()));
        binding.sleepTimer.setOnLongClickListener(view -> {
            vibrationManager.vibrate();

            final NavDirections directions = MainFragmentDirections.actionMainFragmentToSleepTimerConfigFragment();
            final FragmentNavigator.Extras navigatorExtras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(binding.sleepTimer, binding.sleepTimer.getTransitionName())
                    .build();
            navController.navigate(directions, navigatorExtras);

            return true;
        });

        disposable.add(viewModel.getSleepTimer()
                .subscribe(sleepTimer -> binding.sleepTimer.setEnabled(sleepTimer.isEnabled())));

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
    }
}
