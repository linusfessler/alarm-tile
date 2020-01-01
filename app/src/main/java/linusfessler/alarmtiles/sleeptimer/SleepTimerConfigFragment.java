package linusfessler.alarmtiles.sleeptimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.App;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSleepTimerConfigBinding;

public class SleepTimerConfigFragment extends Fragment {

    @Inject
    SleepTimerViewModelFactory sleepTimerViewModelFactory;

    private SleepTimerViewModel viewModel;
    private FragmentSleepTimerConfigBinding binding;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) this.requireActivity().getApplicationContext()).getAppComponent().inject(this);
        this.viewModel = ViewModelProviders.of(this, this.sleepTimerViewModelFactory).get(SleepTimerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_timer_config, container, false);

        this.disposable.add(this.viewModel.getSleepTimer().firstElement().subscribe(this::initializeView));
        this.disposable.add(this.viewModel.getSleepTimer().subscribe(this::registerSleepTimerListeners));
        this.disposable.add(this.viewModel.isResettingVolumeEnabled().subscribe(this.binding.resettingVolume::setEnabled));

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        this.unregisterSleepTimerListeners();
        this.disposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }

    private void initializeView(final SleepTimer sleepTimer) {
        final SleepTimerConfig config = sleepTimer.getConfig();
        this.binding.duration.updateTime(config.getDuration());
        this.binding.fading.setChecked(config.isFading());
        this.binding.resettingVolume.setChecked(config.isResettingVolume());
    }

    private void registerSleepTimerListeners(final SleepTimer sleepTimer) {
        this.binding.duration.setOnTimeChangedListener((hours, minutes) ->
                this.viewModel.setDuration(sleepTimer, hours, minutes));

        this.binding.fading.setOnCheckedChangeListener((buttonView, isChecked) ->
                this.viewModel.setFading(sleepTimer, isChecked));

        this.binding.resettingVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                this.viewModel.setResettingVolume(sleepTimer, isChecked));
    }

    private void unregisterSleepTimerListeners() {
        this.binding.duration.setOnTimeChangedListener(null);
        this.binding.fading.setOnCheckedChangeListener(null);
        this.binding.resettingVolume.setOnCheckedChangeListener(null);
    }
}
