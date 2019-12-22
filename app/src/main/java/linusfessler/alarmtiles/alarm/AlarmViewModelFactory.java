package linusfessler.alarmtiles.alarm;

import android.app.Application;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeOfDayFormatter;

@Singleton
public class AlarmViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final AlarmRepository alarmRepository;
    private final TimeOfDayFormatter timeOfDayFormatter;

    @Inject
    public AlarmViewModelFactory(final Application application, final AlarmRepository alarmRepository, final TimeOfDayFormatter timeOfDayFormatter) {
        this.application = application;
        this.alarmRepository = alarmRepository;
        this.timeOfDayFormatter = timeOfDayFormatter;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AlarmViewModel.class)) {
            final String tileLabel = this.application.getString(R.string.alarm);
            final boolean is24Hours = DateFormat.is24HourFormat(this.application);
            return (T) new AlarmViewModel(this.alarmRepository, tileLabel, this.timeOfDayFormatter, is24Hours);
        }

        throw new IllegalArgumentException(String.format("Unknown view model class: %s.", modelClass));
    }
}
