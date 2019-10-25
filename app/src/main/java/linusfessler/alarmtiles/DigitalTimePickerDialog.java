package linusfessler.alarmtiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

public class DigitalTimePickerDialog {

    private final AlertDialog alertDialog;

    public DigitalTimePickerDialog(final Context context, final TimePicker.OnTimeChangedListener listener,
                                   final int hour, final int minute, final boolean is24HourView) {
        final TimePicker timePicker = (TimePicker) LayoutInflater.from(context).inflate(R.layout.time_picker_digital, null);
        timePicker.setOnTimeChangedListener(listener);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
        timePicker.setIs24HourView(is24HourView);

        alertDialog = new AlertDialog.Builder(context)
                .setView(timePicker)
                .setCancelable(true)
                .create();
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.hide();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

}
