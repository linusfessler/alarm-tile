package linusfessler.alarmtiles.views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.TimeFormatter;
import linusfessler.alarmtiles.TimeOfDayFormatter;

public class TimeSettings extends com.google.android.material.textview.MaterialTextView {

    public enum TimeFormat {TIME, TIME_OF_DAY}

    private TimeOfDayFormatter timeOfDayFormatter;
    private TimeFormatter timeFormatter;

    private TimePickerDialog timeOfDayPickerDialog;
    private DigitalTimePickerDialog timePickerDialog;

    private TimeFormat timeFormat;

    public TimeSettings(@NonNull final Context context) {
        super(context);
        this.init(context, null);
    }

    public TimeSettings(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public TimeSettings(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this.initSelf(context);
        this.initTimeOfDayPickerDialog(context);
        this.initTimePickerDialog(context);
        this.initWithAttrs(context, attrs);
    }

    private void initSelf(@NonNull final Context context) {
        this.timeOfDayFormatter = new TimeOfDayFormatter();
        this.timeFormatter = new TimeFormatter();

        this.setClickable(true);
        this.setFocusable(true);

        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        final Drawable selectableItemBackground = context.getResources().getDrawable(typedValue.resourceId, context.getTheme());

        this.setBackgroundDrawable(selectableItemBackground);
    }

    private void initWithAttrs(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TimeSettings);
        final int hour = styledAttributes.getInt(R.styleable.TimeSettings_hour, 0);
        final int minute = styledAttributes.getInt(R.styleable.TimeSettings_minute, 0);
        final int timeFormatOrdinal = styledAttributes.getInt(R.styleable.TimeSettings_timeFormat, 0);
        final TimeFormat initialTimeFormat = TimeFormat.values()[timeFormatOrdinal];
        styledAttributes.recycle();

        this.updateTime(hour, minute);
        this.setTimeFormat(initialTimeFormat);

        this.setOnClickListener(v -> {
            if (this.timeFormat == TimeFormat.TIME) {
                this.timePickerDialog.show();
            } else {
                this.timeOfDayPickerDialog.show();
            }
        });
    }

    private void initTimeOfDayPickerDialog(@NonNull final Context context) {
        final boolean is24Hours = DateFormat.is24HourFormat(context);
        final TimePickerDialog.OnTimeSetListener listener = (view, newHourOfDay, newMinuteOfHour) ->
                this.setText(this.timeOfDayFormatter.format(newHourOfDay, newMinuteOfHour, is24Hours));
        this.timeOfDayPickerDialog = new TimePickerDialog(context, listener, 0, 0, is24Hours);
    }

    private void initTimePickerDialog(@NonNull final Context context) {
        final TimePicker.OnTimeChangedListener listener = (view, newHours, newMinutes) ->
                this.setText(this.timeFormatter.format(newHours, newMinutes));
        this.timePickerDialog = new DigitalTimePickerDialog(context, listener, 0, 0, true);
    }

    public void updateTime(final int hour, final int minute) {
        this.timeOfDayPickerDialog.updateTime(hour, minute);
        this.timePickerDialog.setHour(hour);
        this.timePickerDialog.setMinute(minute);
    }

    public void setTimeFormat(final TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

}
