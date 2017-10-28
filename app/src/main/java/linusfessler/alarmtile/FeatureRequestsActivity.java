package linusfessler.alarmtile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FeatureRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_requests);
        setTitle(getString(R.string.activity_feature_requests_title));
    }
}