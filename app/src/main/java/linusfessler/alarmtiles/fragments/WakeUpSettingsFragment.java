package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;

public class WakeUpSettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_wake_up_settings, container, false);

        final MaterialButton button = root.findViewById(R.id.next_button);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(WakeUpSettingsFragmentDirections.actionWakeUpSettingsFragmentToSnoozeSettingsFragment()));

        return root;
    }

}
