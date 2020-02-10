package linusfessler.alarmtiles.sleeptimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.databinding.FragmentSleepTimerConfigBinding;

import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.setFadingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.setResettingVolume;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.setTime;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.setTimeUnit;
import static linusfessler.alarmtiles.sleeptimer.SleepTimerEvent.toggle;

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
                    binding.fadingVolume.setChecked(sleepTimer.isFadingVolume());
                    binding.resettingVolume.setChecked(sleepTimer.isResettingVolume());

                    disposable.add(binding.duration.getTimeObservable()
                            .skip(1) // Skip first value (which is the one we just set)
                            .subscribe(time -> viewModel.dispatch(setTime(time))));

                    disposable.add(binding.duration.getTimeUnitObservable()
                            .skip(1) // Skip first value (which is the one we just set)
                            .subscribe(timeUnit -> viewModel.dispatch(setTimeUnit(timeUnit))));

                    binding.fadingVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                            viewModel.dispatch(setFadingVolume(isChecked)));

                    binding.resettingVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                            viewModel.dispatch(setResettingVolume(isChecked)));
                }));

        binding.sleepTimer.setOnClickListener(view -> viewModel.dispatch(toggle()));

        disposable.add(viewModel.getSleepTimer()
                .subscribe(sleepTimer -> {
                    binding.sleepTimer.setEnabled(sleepTimer.isEnabled());
                    binding.resettingVolume.setEnabled(sleepTimer.isFadingVolume());
                }));

        disposable.add(viewModel.getTimeLeft()
                .subscribe(binding.sleepTimer::setSubtitle));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        binding.sleepTimer.setOnClickListener(null);
        binding.fadingVolume.setOnCheckedChangeListener(null);
        binding.resettingVolume.setOnCheckedChangeListener(null);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
