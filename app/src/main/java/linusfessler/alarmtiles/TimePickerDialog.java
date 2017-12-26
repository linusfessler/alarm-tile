package linusfessler.alarmtiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import linusfessler.alarmtiles.R;

/** Have to call setIsAnalog, then setIs24HourView in that order */
public class TimePickerDialog extends android.app.TimePickerDialog {

    private TimePicker timePicker;

    public TimePickerDialog(Context context, boolean isAnalog, boolean is24HourView, int hour, int minute) {
        super(context, null, 0, 0, true);
        update(isAnalog, is24HourView, hour, minute);
        /*Window window = getWindow();
        if (window != null) {
            //window.setBackgroundDrawableResource(R.color.windowBackground);
        }*/
    }

    public void update(boolean isAnalog, boolean is24HourView, int hour, int minute) {
        int layoutId;
        if (isAnalog) {
            layoutId = R.layout.time_picker_analog;
        } else {
            layoutId = R.layout.time_picker_digital;
        }
        if (timePicker != null) {
            timePicker.invalidate();
        }
        timePicker = (TimePicker) LayoutInflater.from(getContext()).inflate(layoutId, null);
        timePicker.setIs24HourView(is24HourView);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
        setView(timePicker);
    }

    public int getHour() {
        return timePicker.getHour();
    }

    public int getMinute() {
        return timePicker.getMinute();
    }
}
