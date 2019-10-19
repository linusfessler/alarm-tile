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

public class AdvancedSettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_advanced_settings, container, false);

        final MaterialButton button = root.findViewById(R.id.finish_button);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_advancedSettingsFragment_to_mainFragment));

        return root;
    }

}
