package linusfessler.alarmtiles;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class LocalTimePicker extends TimePicker {

    public LocalTimePicker(Context context) {
        super(context);
        init(context);
    }

    public LocalTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocalTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LocalTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setIs24HourView(DateFormat.is24HourFormat(context));
    }

}