package linusfessler.alarmtiles.activities;

import android.app.Activity;
import android.os.Bundle;

import linusfessler.alarmtiles.R;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.activity_about_title));
    }
}