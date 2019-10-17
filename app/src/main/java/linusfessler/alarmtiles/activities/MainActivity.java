package linusfessler.alarmtiles.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

import linusfessler.alarmtiles.R;

public class MainActivity extends FragmentActivity implements NavController.OnDestinationChangedListener {

    private MaterialToolbar toolbar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        navController.removeOnDestinationChangedListener(this);
        super.onDestroy();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        CharSequence title = destination.getLabel();
        toolbar.setTitle(title);
    }
}
