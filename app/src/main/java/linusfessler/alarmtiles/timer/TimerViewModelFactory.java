package linusfessler.alarmtiles.timer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeFormatter;

@Singleton
public class TimerViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final TimerRepository timerRepository;
    private final TimeFormatter timeFormatter;

    @Inject
    public TimerViewModelFactory(final Application application, final TimerRepository timerRepository, final TimeFormatter timeFormatter) {
        this.application = application;
        this.timerRepository = timerRepository;
        this.timeFormatter = timeFormatter;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TimerViewModel.class)) {
            final String tileLabel = this.application.getString(R.string.timer);
            return (T) new TimerViewModel(this.timerRepository, tileLabel, this.timeFormatter);
        }

        throw new IllegalArgumentException(String.format("Unknown view model class: %s.", modelClass));
    }
}
