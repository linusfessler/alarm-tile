package linusfessler.alarmtile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;

import linusfessler.alarmtile.services.AlarmService;
import linusfessler.alarmtile.constants.BroadcastActions;
import linusfessler.alarmtile.R;
import linusfessler.alarmtile.utility.TimeFormatter;

public class AlarmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Calendar calendar = Calendar.getInstance();
        TextView time = findViewById(R.id.time);
        time.setText(TimeFormatter.format(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_START));
        startService(new Intent(this, AlarmService.class));
    }

    private void snooze() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_SNOOZE));
        stopService(new Intent(this, AlarmService.class));
        finishAndRemoveTask();
    }

    private void dismiss() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastActions.ALARM_DISMISS));
        stopService(new Intent(this, AlarmService.class));
        finishAndRemoveTask();
    }

    public void onSnooze(View view) {
        snooze();
    }

    public void onDismiss(View view) {
        dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        dismiss();
    }
}
