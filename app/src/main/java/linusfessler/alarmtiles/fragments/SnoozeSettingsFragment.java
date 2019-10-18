package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;

public class SnoozeSettingsFragment extends Fragment {

    private AlarmTile alarmTile;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = SnoozeSettingsFragmentArgs.fromBundle(requireArguments()).getAlarmTile();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_snooze_settings, container, false);

        final MaterialButton button = root.findViewById(R.id.next_button);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(SnoozeSettingsFragmentDirections.actionSnoozeSettingsFragmentToAdvancedSettingsFragment(alarmTile)));

        return root;
    }

}
