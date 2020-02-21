package linusfessler.alarmtiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

public class DigitalTimePickerDialog {

    private final TimePicker timePicker;
    private final AlertDialog alertDialog;

    @SuppressLint("InflateParams")
    public DigitalTimePickerDialog(final Context context, final TimePicker.OnTimeChangedListener listener,
                                   final int hour, final int minute, final boolean is24HourView) {
        timePicker = (TimePicker) LayoutInflater.from(context).inflate(R.layout.time_picker_digital, null, false);
        timePicker.setOnTimeChangedListener(listener);
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
        timePicker.setIs24HourView(is24HourView);

        alertDialog = new AlertDialog.Builder(context)
                .setView(timePicker)
                .setCancelable(true)
                .create();
    }

    public int getHour() {
        return timePicker.getHour();
    }

    public int getMinute() {
        return timePicker.getMinute();
    }

    public void setHour(final int hour) {
        timePicker.setHour(hour);
    }

    public void setMinute(final int minute) {
        timePicker.setMinute(minute);
    }

    public void setIs24HourView(final boolean is24HourView) {
        timePicker.setIs24HourView(is24HourView);
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

    public void setOnCancelListener(final DialogInterface.OnCancelListener listener) {
        alertDialog.setOnCancelListener(listener);
    }

    public void setOnDismissListener(final DialogInterface.OnDismissListener listener) {
        alertDialog.setOnDismissListener(listener);
    }
}
