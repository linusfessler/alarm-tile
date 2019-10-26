package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import linusfessler.alarmtiles.DigitalTimePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSleepSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.SleepSettingsViewModel;

public class SleepSettingsFragment extends SettingsFragment implements TimePicker.OnTimeChangedListener {

    private static final String ALARM_TILE_ARG_NAME = "alarm_tile";
    private static final String ACTION_ZEN_MODE_PRIORITY_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";

    private AlarmTile alarmTile;
    private SleepSettingsViewModel viewModel;
    private DigitalTimePickerDialog timePickerDialog;

    private MaterialTextView timerDuration;
    private MaterialButton openSettingsButton;

    public static SleepSettingsFragment newInstance(final AlarmTile alarmTile) {
        final SleepSettingsFragment fragment = new SleepSettingsFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ALARM_TILE_ARG_NAME, alarmTile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.sleep_settings_title;
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

        viewModel = ViewModelProviders.of(this).get(SleepSettingsViewModel.class);
        viewModel.setAlarmTile(alarmTile);
        final FragmentSleepSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_settings, container, false);
        binding.setViewModel(viewModel);

        timerDuration = binding.timerDuration;
        openSettingsButton = binding.openSettingsButton;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTimePicker();
        initOpenSettingsButton();
    }

    private void initTimePicker() {
        final Context context = requireContext();
        final int hours = viewModel.getTimerHours();
        final int minutes = viewModel.getTimerMinutes();
        timePickerDialog = new DigitalTimePickerDialog(context, this, hours, minutes, true);
        timerDuration.setOnClickListener(v -> timePickerDialog.show());
    }

    private void initOpenSettingsButton() {
        openSettingsButton.setOnClickListener(v -> {
            final Intent intent = new Intent(ACTION_ZEN_MODE_PRIORITY_SETTINGS);
            startActivity(intent);
        });
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
