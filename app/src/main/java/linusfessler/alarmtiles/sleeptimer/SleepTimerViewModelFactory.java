package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeFormatter;

@Singleton
public class SleepTimerViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final SleepTimerService sleepTimerService;
    private final TimeFormatter timeFormatter;

    @Inject
    public SleepTimerViewModelFactory(final Application application, final SleepTimerService sleepTimerService, final TimeFormatter timeFormatter) {
        this.application = application;
        this.sleepTimerService = sleepTimerService;
        this.timeFormatter = timeFormatter;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SleepTimerViewModel.class)) {
            final String tileLabel = this.application.getString(R.string.sleep_timer);
            return (T) new SleepTimerViewModel(this.sleepTimerService, tileLabel, this.timeFormatter);
        }

        throw new IllegalArgumentException(String.format("Unknown view model class: %s.", modelClass));
    }
}
