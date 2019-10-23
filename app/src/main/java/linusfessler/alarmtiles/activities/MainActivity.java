package linusfessler.alarmtiles.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import linusfessler.alarmtiles.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
