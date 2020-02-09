package linusfessler.alarmtiles.alarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Singleton;

import linusfessler.alarmtiles.shared.TimeOfDayFormatter;

@Singleton
public class AlarmViewModel extends ViewModel {

    private final AlarmRepository repository;
    private final String tileLabel;
    private final TimeOfDayFormatter timeOfDayFormatter;
    private final boolean is24Hours;

    private final LiveData<Alarm> alarmLiveData;
    private final LiveData<String> tileLabelLiveData;

    public AlarmViewModel(final AlarmRepository repository, final String tileLabel, final TimeOfDayFormatter timeOfDayFormatter, final boolean is24Hours) {
        this.repository = repository;
        this.tileLabel = tileLabel;
        this.timeOfDayFormatter = timeOfDayFormatter;
        this.is24Hours = is24Hours;

        alarmLiveData = this.repository.getAlarm();

        tileLabelLiveData = Transformations.switchMap(alarmLiveData, alarm -> {
            final MutableLiveData<String> tileLabelMutableLiveData = new MutableLiveData<>();

            if (alarm == null) {
                return tileLabelMutableLiveData;
            }

            if (!alarm.isEnabled()) {
                tileLabelMutableLiveData.postValue(tileLabel);
                return tileLabelMutableLiveData;
            }

            final String timeOfDay = AlarmViewModel.this.timeOfDayFormatter.format(alarm.getHourOfDay(), alarm.getMinuteOfHour(), is24Hours);
            tileLabelMutableLiveData.postValue(tileLabel + "\n" + timeOfDay);
            return tileLabelMutableLiveData;
        });
    }

    public LiveData<Alarm> getAlarm() {
        return alarmLiveData;
    }

    public LiveData<String> getTileLabel() {
        return tileLabelLiveData;
    }

    public void toggle(final Alarm alarm) {
        alarm.toggle();
        repository.update(alarm);
    }
}
