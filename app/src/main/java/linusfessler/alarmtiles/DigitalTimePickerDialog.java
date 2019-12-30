package linusfessler.alarmtiles;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

public class DigitalTimePickerDialog {

    private final TimePicker timePicker;
    private final AlertDialog alertDialog;

    public DigitalTimePickerDialog(final Context context, final TimePicker.OnTimeChangedListener listener,
                                   final int hour, final int minute, final boolean is24HourView) {
        this.timePicker = (TimePicker) LayoutInflater.from(context).inflate(R.layout.time_picker_digital, null, false);
        this.timePicker.setOnTimeChangedListener(listener);
        this.timePicker.setHour(hour);
        this.timePicker.setMinute(minute);
        this.timePicker.setIs24HourView(is24HourView);

        this.alertDialog = new AlertDialog.Builder(context)
                .setView(this.timePicker)
                .setCancelable(true)
                .create();
    }

    public int getHour() {
        return this.timePicker.getHour();
    }

    public int getMinute() {
        return this.timePicker.getMinute();
    }

    public void setHour(final int hour) {
        this.timePicker.setHour(hour);
    }

    public void setMinute(final int minute) {
        this.timePicker.setMinute(minute);
    }

    public void setIs24HourView(final boolean is24HourView) {
        this.timePicker.setIs24HourView(is24HourView);
    }

    public void show() {
        this.alertDialog.show();
    }

    public void hide() {
        this.alertDialog.hide();
    }

    public void dismiss() {
        this.alertDialog.dismiss();
    }

    public void setOnCancelListener(final DialogInterface.OnCancelListener listener) {
        this.alertDialog.setOnCancelListener(listener);
    }

    public void setOnDismissListener(final DialogInterface.OnDismissListener listener) {
        this.alertDialog.setOnDismissListener(listener);
    }
}
