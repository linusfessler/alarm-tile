package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;

public class WakeUpSettingsFragment extends Fragment implements TimePicker.OnTimeChangedListener {

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_wake_up_settings, container, false);

        final MaterialButton button = root.findViewById(R.id.next_button);
        button.setOnClickListener(Navigation.createNavigateOnClickListener(WakeUpSettingsFragmentDirections.actionWakeUpSettingsFragmentToSnoozeSettingsFragment()));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initTimePicker(view);
    }

    /*private void initTimePicker(final View root) {
        final Context context = requireContext();
        final boolean is24HourView = DateFormat.is24HourFormat(context);
        final int hours = viewModel.getHours();
        final int minutes = viewModel.getMinutes();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, hours, minutes, true);

        final MaterialTextView time = root.findViewById(R.id.time);
        time.setOnClickListener(v -> timePickerDialog.show());
    }*/

    @Override
    public void onTimeChanged(final TimePicker view, final int hours, final int minute) {
        //viewModel.setHours(hours);
        //viewModel.setMinutes(minute);
    }

}
