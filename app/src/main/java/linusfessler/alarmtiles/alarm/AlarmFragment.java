package linusfessler.alarmtiles.alarm;

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
import linusfessler.alarmtiles.databinding.FragmentAlarmBinding;

public class AlarmFragment extends Fragment {

    @Inject
    AlarmViewModelFactory viewModelFactory;

    private AlarmViewModel viewModel;
    private FragmentAlarmBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) requireActivity().getApplicationContext())
                .getAlarmComponent()
                .inject(this);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(AlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentAlarmBinding.inflate(inflater, container, false);

        viewModel.getAlarm().observe(getViewLifecycleOwner(), alarm -> {
            if (alarm == null) {
                return;
            }
            binding.alarm.setEnabled(alarm.isEnabled());
            binding.alarm.setOnClickListener(view -> viewModel.toggle(alarm));
        });

        viewModel.getTileLabel().observe(getViewLifecycleOwner(), binding.alarm::setLabel);

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
