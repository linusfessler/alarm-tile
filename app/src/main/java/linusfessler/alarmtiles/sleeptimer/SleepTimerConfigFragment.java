package linusfessler.alarmtiles.sleeptimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.databinding.FragmentSleepTimerConfigBinding;

public class SleepTimerConfigFragment extends Fragment {

    @Inject
    SleepTimerViewModel viewModel;

    private FragmentSleepTimerConfigBinding binding;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) requireActivity().getApplicationContext())
                .getAppComponent()
                .inject(this);

        setSharedElementEnterTransition(TransitionInflater
                .from(requireContext())
                .inflateTransition(android.R.transition.move));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_timer_config, container, false);

        disposable.add(viewModel.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    binding.duration.setTime(sleepTimer.getTime());
                    binding.duration.setTimeUnit(sleepTimer.getTimeUnit());
                    binding.decreasingVolume.setChecked(sleepTimer.isDecreasingVolume());

                    disposable.add(binding.duration.getTimeObservable()
                            .skip(1) // Skip first value (which is the one we just set)
                            .subscribe(time -> viewModel.dispatch(new SleepTimerEvent.SetTime(time))));

                    disposable.add(binding.duration.getTimeUnitObservable()
                            .skip(1) // Skip first value (which is the one we just set)
                            .subscribe(timeUnit -> viewModel.dispatch(new SleepTimerEvent.SetTimeUnit(timeUnit))));

                    binding.decreasingVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                            viewModel.dispatch(new SleepTimerEvent.SetDecreasingVolume(isChecked)));
                }));

        binding.sleepTimer.setOnClickListener(view -> viewModel.dispatch(new SleepTimerEvent.Toggle()));

        disposable.add(viewModel.getSleepTimer()
                .subscribe(sleepTimer -> binding.sleepTimer.setEnabled(sleepTimer.isEnabled())));

        disposable.add(viewModel.getTimeLeft()
                .subscribe(binding.sleepTimer::setSubtitle));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        binding.sleepTimer.setOnClickListener(null);
        binding.decreasingVolume.setOnCheckedChangeListener(null);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
