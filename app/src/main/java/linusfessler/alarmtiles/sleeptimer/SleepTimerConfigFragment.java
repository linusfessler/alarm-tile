package linusfessler.alarmtiles.sleeptimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSleepTimerConfigBinding;
import linusfessler.alarmtiles.sleeptimer.model.SleepTimerConfig;

public class SleepTimerConfigFragment extends Fragment {

    @Inject
    SleepTimerViewModelFactory sleepTimerViewModelFactory;

    private SleepTimerViewModel viewModel;
    private FragmentSleepTimerConfigBinding binding;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) this.requireActivity().getApplicationContext())
                .getAppComponent()
                .inject(this);

        this.viewModel = new ViewModelProvider(this, this.sleepTimerViewModelFactory).get(SleepTimerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_timer_config, container, false);

        this.disposable.add(this.viewModel.getSleepTimer()
                .firstElement()
                .subscribe(sleepTimer -> {
                    final SleepTimerConfig config = sleepTimer.getConfig();
                    this.binding.duration.setMillis(config.getDuration());
                    this.binding.duration.setTimeUnit(config.getTimeUnit());
                    this.binding.fadingVolume.setChecked(config.isFadingVolume());
                    this.binding.resettingVolume.setChecked(config.isResettingVolume());
                }));

        this.binding.sleepTimer.setOnClickListener(view -> this.viewModel.onClick());

        this.disposable.add(this.viewModel.getSleepTimer()
                .subscribe(sleepTimer -> this.binding.sleepTimer.setEnabled(sleepTimer.getState() == SleepTimerState.RUNNING)));

        this.disposable.add(this.viewModel.getTimeLeft()
                .subscribe(this.binding.sleepTimer::setSubtitle));

        this.disposable.add(this.binding.duration.getMillisObservable()
                .subscribe(this.viewModel::setDuration));

        this.disposable.add(this.binding.duration.getTimeUnitObservable()
                .subscribe(this.viewModel::setTimeUnit));

        this.binding.fadingVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                this.viewModel.setFadingVolume(isChecked));

        this.binding.resettingVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                this.viewModel.setResettingVolume(isChecked));

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        this.disposable.clear();

        this.binding.fadingVolume.setOnCheckedChangeListener(null);
        this.binding.resettingVolume.setOnCheckedChangeListener(null);

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }
}
