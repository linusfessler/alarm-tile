package linusfessler.alarmtiles;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainTabConfigurationStrategy implements TabLayoutMediator.TabConfigurationStrategy {

    @Override
    public void onConfigureTab(@NonNull final TabLayout.Tab tab, final int position) {
        switch (position) {
            case 0:
                tab.setIcon(R.drawable.ic_music_off_24px);
                break;
            case 1:
                tab.setIcon(R.drawable.ic_timer_24px);
                tab.setText(R.string.timer);
                break;
            default:
                throw new IllegalStateException("Should never happen");
        }
    }

}
