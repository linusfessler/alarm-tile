package linusfessler.alarmtiles;

import android.content.Context;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

public class DigitalTimePickerDialog extends AlertDialog {

    public DigitalTimePickerDialog(final Context context, final TimePicker.OnTimeChangedListener listener,
                                   final int hour, final int minute, final boolean is24HourView) {
        super(context);

        TimePicker timePicker = (TimePicker) getLayoutInflater().inflate(R.layout.time_picker_digital, null);

        timePicker.setOnTimeChangedListener(listener);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
        timePicker.setIs24HourView(is24HourView);

        setView(timePicker);
    }

}
