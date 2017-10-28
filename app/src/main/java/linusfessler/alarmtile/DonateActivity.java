package linusfessler.alarmtile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        setTitle(getString(R.string.activity_donate_title));
    }
}