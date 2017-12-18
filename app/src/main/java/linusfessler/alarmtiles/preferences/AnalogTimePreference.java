package linusfessler.alarmtiles.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import linusfessler.alarmtiles.utility.TimeFormatter;

public class AnalogTimePreference extends TimePreference {

    private Context context;

    public AnalogTimePreference(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.context = context;
    }

    @Override
    protected View onCreateDialogView() {
        timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        return timePicker;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle("");
    }

    @Override
    public CharSequence getSummary() {
        return TimeFormatter.format(hours, minutes);
    }
}