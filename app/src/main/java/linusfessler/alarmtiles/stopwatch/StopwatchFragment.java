package linusfessler.alarmtiles.stopwatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import linusfessler.alarmtiles.core.App;
import linusfessler.alarmtiles.databinding.FragmentStopwatchBinding;

public class StopwatchFragment extends Fragment {

    @Inject
    StopwatchViewModelFactory viewModelFactory;

    private StopwatchViewModel viewModel;

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) requireActivity().getApplicationContext())
                .getStopwatchComponent()
                .inject(this);

        viewModel = viewModelFactory.create(StopwatchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentStopwatchBinding binding = FragmentStopwatchBinding.inflate(inflater, container, false);

        binding.stopwatch.setOnClickListener(view -> viewModel.onClick());

        disposable.add(viewModel.isEnabled()
                .subscribe(binding.stopwatch::setEnabled));

        disposable.add(viewModel.getElapsedTime()
                .subscribe(binding.stopwatch::setSubtitle));

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
