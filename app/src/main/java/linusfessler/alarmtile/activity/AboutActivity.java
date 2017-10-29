package linusfessler.alarmtile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import linusfessler.alarmtile.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.activity_about_title));
    }
}