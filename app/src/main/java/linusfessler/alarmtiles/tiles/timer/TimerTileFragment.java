package linusfessler.alarmtiles.tiles.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.databinding.FragmentTimerTileBinding;

public class TimerTileFragment extends Fragment {

    @Inject
    TimerViewModelFactory viewModelFactory;

    private TimerViewModel viewModel;
    private FragmentTimerTileBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) requireActivity().getApplicationContext())
                .getTimerComponent()
                .inject(this);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(TimerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentTimerTileBinding.inflate(inflater, container, false);

        viewModel.getTimer().observe(getViewLifecycleOwner(), timer -> {
            if (timer == null) {
                return;
            }
            binding.timer.setEnabled(timer.isEnabled());
            binding.timer.setOnClickListener(view -> viewModel.toggle(timer));
        });

        viewModel.getTileLabel().observe(getViewLifecycleOwner(), binding.timer::setLabel);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
