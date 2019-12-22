package linusfessler.alarmtiles.stopwatch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeFormatter;

public class StopwatchViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final StopwatchRepository stopwatchRepository;
    private final TimeFormatter timeFormatter;

    @Inject
    public StopwatchViewModelFactory(final Application application, final StopwatchRepository stopwatchRepository, final TimeFormatter timeFormatter) {
        this.application = application;
        this.stopwatchRepository = stopwatchRepository;
        this.timeFormatter = timeFormatter;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StopwatchViewModel.class)) {
            final String tileLabel = this.application.getString(R.string.stopwatch);
            return (T) new StopwatchViewModel(this.stopwatchRepository, tileLabel, this.timeFormatter);
        }

        throw new IllegalArgumentException(String.format("Unknown view model class: %s.", modelClass));
    }
}
