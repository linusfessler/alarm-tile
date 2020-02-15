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
                // Initialize view, afterwards it matches the user input already
                .firstElement()
                .subscribe(sleepTimer -> {
                    binding.decreaseVolume.setChecked(sleepTimer.isDecreasingVolume());
                    binding.decreaseVolume.setOnCheckedChangeListener((buttonView, isChecked) ->
                            viewModel.dispatch(new SleepTimerEvent.SetDecreasingVolume(isChecked)));
                }));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
        binding.decreaseVolume.setOnCheckedChangeListener(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
