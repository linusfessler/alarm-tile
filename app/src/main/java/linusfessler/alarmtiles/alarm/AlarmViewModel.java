package linusfessler.alarmtiles.alarm;

import android.app.Application;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeOfDayFormatter;

public class AlarmViewModel extends AndroidViewModel {

    private final AlarmRepository repository;

    private final LiveData<Alarm> alarmLiveData;
    private final LiveData<String> tileLabelLiveData;

    public AlarmViewModel(@NonNull final Application application) {
        super(application);
        this.repository = new AlarmRepository(application);

        this.alarmLiveData = this.repository.getAlarm();

        final String tileLabel = application.getString(R.string.alarm);
        final boolean is24Hours = DateFormat.is24HourFormat(application);
        final TimeOfDayFormatter timeOfDayFormatter = new TimeOfDayFormatter();
        this.tileLabelLiveData = Transformations.switchMap(this.alarmLiveData, alarm -> {
            final MutableLiveData<String> tileLabelMutableLiveData = new MutableLiveData<>();

            if (alarm == null) {
                return tileLabelMutableLiveData;
            }

            if (!alarm.isEnabled()) {
                tileLabelMutableLiveData.postValue(tileLabel);
                return tileLabelMutableLiveData;
            }

            final String timeOfDay = timeOfDayFormatter.format(alarm.getHourOfDay(), alarm.getMinuteOfHour(), is24Hours);
            tileLabelMutableLiveData.postValue(tileLabel + "\n" + timeOfDay);
            return tileLabelMutableLiveData;
        });
    }

    public LiveData<Alarm> getAlarm() {
        return this.alarmLiveData;
    }

    public LiveData<String> getTileLabel() {
        return this.tileLabelLiveData;
    }

    public void toggle(final Alarm alarm) {
        alarm.toggle();
        this.repository.updateAlarm(alarm);
    }
}
