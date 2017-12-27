package linusfessler.alarmtiles;

import android.content.Context;
import android.widget.TimePicker;

/** Have to call setIsAnalog, then setIs24HourView in that order */
public class TimePickerDialog extends android.app.TimePickerDialog {

    private TimePicker timePicker;

    public TimePickerDialog(Context context, boolean isAnalog, boolean is24HourView, int hour, int minute) {
        super(context, null, hour, minute, is24HourView);

        int layoutId;
        if (isAnalog) {
            layoutId = R.layout.time_picker_analog;
        } else {
            layoutId = R.layout.time_picker_digital;
        }
        timePicker = (TimePicker) getLayoutInflater().inflate(layoutId, null);
        timePicker.setIs24HourView(is24HourView);
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
