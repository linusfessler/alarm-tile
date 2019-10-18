package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.DrawablePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.NewAlarmFragmentBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.model.BasicSettings;
import linusfessler.alarmtiles.viewmodel.BasicSettingsViewModel;

public class BasicSettingsFragment extends Fragment implements DrawablePickerDialog.OnDrawablePickedListener {

    private static final int[] ICON_RESOURCE_IDS = {
            R.drawable.ic_alarm_24px,
            R.drawable.ic_alarm_off_24px,
            R.drawable.ic_timer_24px,
            R.drawable.ic_timer_off_24px,
            R.drawable.ic_snooze_24px,
            R.drawable.ic_schedule_24px,
            R.drawable.ic_info_24px,
            R.drawable.ic_add_24px,
            R.drawable.ic_delete_24px,
            R.drawable.ic_music_off_24px,
            R.drawable.ic_notifications_active_24px,
    };

    private static final long SHOW_ICON_RIPPLE_AFTER_MILLISECONDS = 750;
    private static final long HIDE_ICON_RIPPLE_AFTER_MILLISECONDS = 250;

    private AlarmTile alarmTile;
    private BasicSettingsViewModel viewModel;
    private DrawablePickerDialog iconPickerDialog;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = BasicSettingsFragmentArgs.fromBundle(requireArguments()).getAlarmTile();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(BasicSettingsViewModel.class);

        if (alarmTile.getBasicSettings() != null) {
            final BasicSettings basicSettings = alarmTile.getBasicSettings();
            viewModel.setName(basicSettings.getName());
            viewModel.setIconResourceId(basicSettings.getIconResourceId());
        }

        final NewAlarmFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.new_alarm_fragment, container, false);
        binding.setViewModel(viewModel);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireActivity())
                        .setTitle("Go back?")
                        .setMessage("Your current changes will be discarded")
                        .setPositiveButton("Yes", (dialog, which) -> Navigation.findNavController(binding.getRoot()).popBackStack())
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        final Context context = requireContext();
        final Resources resources = getResources();
        final String title = resources.getString(R.string.dialog_icon_picker_title);
        final int size = resources.getDimensionPixelSize(R.dimen.icon_size);
        final int color = resources.getColor(R.color.white, context.getTheme());
        iconPickerDialog = new DrawablePickerDialog(context, title, ICON_RESOURCE_IDS, size, size, color);
        iconPickerDialog.addListener(this);

        final ImageView icon = binding.getRoot().findViewById(R.id.icon);
        icon.setOnClickListener(view -> iconPickerDialog.show());

        final Handler handler = new Handler();
        handler.postDelayed(() -> showRipple(icon.getBackground()), SHOW_ICON_RIPPLE_AFTER_MILLISECONDS);
        handler.postDelayed(() -> hideRipple(icon.getBackground()), SHOW_ICON_RIPPLE_AFTER_MILLISECONDS + HIDE_ICON_RIPPLE_AFTER_MILLISECONDS);

        final MaterialButton button = binding.getRoot().findViewById(R.id.next_button);
        button.setOnClickListener(v -> {
            final BasicSettings basicSettings = BasicSettings.builder()
                    .name(viewModel.getName())
                    .iconResourceId(viewModel.getIconResourceId())
                    .build();
            alarmTile.setBasicSettings(basicSettings);

            final NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(BasicSettingsFragmentDirections.actionBasicSettingsFragmentToFallAsleepSettingsFragment(alarmTile));
        });

        return binding.getRoot();
    }

    private void showRipple(final Drawable ripple) {
        ripple.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
    }

    private void hideRipple(final Drawable ripple) {
        ripple.setState(new int[]{});
    }

    @Override
    public void onDestroyView() {
        iconPickerDialog.removeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onDrawablePicked(final int resourceId) {
        viewModel.setIconResourceId(resourceId);
    }
}
