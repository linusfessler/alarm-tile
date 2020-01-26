package linusfessler.alarmtiles.stopwatch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import linusfessler.alarmtiles.TimeFormatter;

public class StopwatchViewModelFactory implements ViewModelProvider.Factory {

    private final StopwatchRepository stopwatchRepository;
    private final TimeFormatter timeFormatter;

    @Inject
    public StopwatchViewModelFactory(final StopwatchRepository stopwatchRepository, final TimeFormatter timeFormatter) {
        this.stopwatchRepository = stopwatchRepository;
        this.timeFormatter = timeFormatter;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StopwatchViewModel.class)) {
            return (T) new StopwatchViewModel(this.stopwatchRepository, this.timeFormatter);
        }

        throw new IllegalArgumentException(String.format("Unknown view model class: %s.", modelClass));
    }
}
