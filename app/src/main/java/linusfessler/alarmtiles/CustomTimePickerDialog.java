package linusfessler.alarmtiles;

import android.content.Context;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

public class CustomTimePickerDialog extends AlertDialog {

    private TimePicker timePicker;

    public CustomTimePickerDialog(Context context, boolean isAnalog, boolean is24HourView, int hour, int minute) {
        super(context);

        int layoutId;
        if (isAnalog) {
            layoutId = R.layout.time_picker_analog;
        } else {
            layoutId = R.layout.time_picker_digital;
        }
        timePicker = (TimePicker) getLayoutInflater().inflate(layoutId, null);
        timePicker.setIs24HourView(is24HourView); // Default should be DateFormat.is24HourFormat(getActivity()
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
        setView(timePicker);

        /*Window window = getWindow();
        if (window != null) {
            //window.setBackgroundDrawableResource(R.color.windowBackground);
        }*/
    }

    public int getHour() {
        return timePicker.getHour();
    }

    public int getMinute() {
        return timePicker.getMinute();
    }
}
