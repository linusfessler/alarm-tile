package linusfessler.alarmtiles.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import linusfessler.alarmtiles.core.MainActivity;

public class LauncherActivity extends Activity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}