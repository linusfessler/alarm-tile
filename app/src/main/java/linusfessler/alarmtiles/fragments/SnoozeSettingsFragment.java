package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSnoozeSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.SnoozeSettingsViewModel;

public class SnoozeSettingsFragment extends SettingsFragment implements TimePicker.OnTimeChangedListener {

    private static final String ALARM_TILE_ARG_NAME = "alarm_tile";

    private AlarmTile alarmTile;
    private SnoozeSettingsViewModel viewModel;
    private DigitalTimePickerDialog timePickerDialog;

    public static SnoozeSettingsFragment newInstance(final AlarmTile alarmTile) {
        final SnoozeSettingsFragment fragment = new SnoozeSettingsFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ALARM_TILE_ARG_NAME, alarmTile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.snooze_settings_title;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = (AlarmTile) requireArguments().get(ALARM_TILE_ARG_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(SnoozeSettingsViewModel.class);
        viewModel.setAlarmTile(alarmTile);
        final FragmentSnoozeSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_snooze_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTimePicker(view);
    }

    private void initTimePicker(final View root) {
        final Context context = requireContext();
        final int hours = viewModel.getSnoozeHours();
        final int minutes = viewModel.getSnoozeMinutes();

        timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);

        final MaterialTextView snoozeDuration = root.findViewById(R.id.snooze_duration);
        snoozeDuration.setOnClickListener(v -> timePickerDialog.show());
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int snoozeHours, final int snoozeMinutes) {
        viewModel.setSnoozeHours(snoozeHours);
        viewModel.setSnoozeMinutes(snoozeMinutes);
    }

    @Override
    public void onDestroyView() {
        timePickerDialog.dismiss();
        super.onDestroyView();
    }

}
