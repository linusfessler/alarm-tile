package linusfessler.alarmtiles.sleeptimer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import linusfessler.alarmtiles.MediaPlaybackManager;
import linusfessler.alarmtiles.MediaVolumeManager;
import linusfessler.alarmtiles.TimeFormatter;

public class SleepTimerViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final SleepTimerRepository sleepTimerRepository;
    private final TimeFormatter timeFormatter;
    private final MediaVolumeManager mediaVolumeManager;
    private final MediaPlaybackManager mediaPlaybackManager;

    @Inject
    public SleepTimerViewModelFactory(final Application application, final SleepTimerRepository sleepTimerRepository, final TimeFormatter timeFormatter,
                                      final MediaVolumeManager mediaVolumeManager, final MediaPlaybackManager mediaPlaybackManager) {
        this.application = application;
        this.sleepTimerRepository = sleepTimerRepository;
        this.timeFormatter = timeFormatter;
        this.mediaVolumeManager = mediaVolumeManager;
        this.mediaPlaybackManager = mediaPlaybackManager;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SleepTimerViewModel.class)) {
            return (T) new SleepTimerViewModel(this.application, this.sleepTimerRepository, this.timeFormatter, this.mediaVolumeManager, this.mediaPlaybackManager);
        }

        throw new IllegalArgumentException(String.format("Unknown view model class: %s.", modelClass));
    }
}
