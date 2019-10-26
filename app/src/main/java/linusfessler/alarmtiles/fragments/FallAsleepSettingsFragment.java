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
import linusfessler.alarmtiles.databinding.FragmentFallAsleepSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.FallAsleepSettingsViewModel;

public class FallAsleepSettingsFragment extends SettingsFragment implements TimePicker.OnTimeChangedListener {

    private static final String ALARM_TILE_ARG_NAME = "alarm_tile";

    private AlarmTile alarmTile;
    private FallAsleepSettingsViewModel viewModel;
    private DigitalTimePickerDialog timePickerDialog;

    public static FallAsleepSettingsFragment newInstance(final AlarmTile alarmTile) {
        final FallAsleepSettingsFragment fragment = new FallAsleepSettingsFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ALARM_TILE_ARG_NAME, alarmTile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.fall_asleep_settings_title;
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

        viewModel = ViewModelProviders.of(this).get(FallAsleepSettingsViewModel.class);
        viewModel.setAlarmTile(alarmTile);
        final FragmentFallAsleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fall_asleep_settings, container, false);
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
        final int hours = viewModel.getTimerHours();
        final int minutes = viewModel.getTimerMinutes();

        timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);

        final MaterialTextView timerDuration = root.findViewById(R.id.timer_duration);
        timerDuration.setOnClickListener(v -> timePickerDialog.show());
    }

    @Override
    public void onTimeChanged(final TimePicker view, final int timerHours, final int timerMinutes) {
        viewModel.setTimerHours(timerHours);
        viewModel.setTimerMinutes(timerMinutes);
    }

    @Override
    public void onDestroyView() {
        timePickerDialog.dismiss();
        super.onDestroyView();
    }
}
