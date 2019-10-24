package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import linusfessler.alarmtiles.DrawablePickerDialog;
import linusfessler.alarmtiles.DrawableStateController;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentGeneralSettingsBinding;
import linusfessler.alarmtiles.viewmodel.GeneralSettingsViewModel;

public class GeneralSettingsFragment extends SettingsFragment implements DrawablePickerDialog.OnDrawablePickedListener {

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

    private static final long SHOW_ICON_RIPPLE_AFTER_MILLISECONDS = 1000;
    private static final long HIDE_ICON_RIPPLE_AFTER_MILLISECONDS = 250;

    private GeneralSettingsViewModel viewModel;
    private DrawablePickerDialog iconPickerDialog;
    private DrawableStateController drawableStateController;

    @Override
    public int getTitleResourceId() {
        return R.string.general_settings_title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(GeneralSettingsViewModel.class);
        final FragmentGeneralSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBackConfirmationDialog(view);
        initIconPicker(view);
    }

    @Override
    public void onDrawablePicked(final int resourceId) {
        viewModel.setIconResourceId(resourceId);
    }

    private void initBackConfirmationDialog(final View root) {
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.dialog_back_title)
                        .setMessage(R.string.dialog_back_message)
                        .setPositiveButton(R.string.dialog_yes, (dialog, which) -> Navigation.findNavController(root).popBackStack())
                        .setNegativeButton(R.string.dialog_no, null)
                        .show();
            }
        });
    }

    private void initIconPicker(final View root) {
        final Context context = requireContext();
        final Resources resources = getResources();
        final String title = resources.getString(R.string.dialog_icon_picker_title);
        final int size = resources.getDimensionPixelSize(R.dimen.icon_size);
        final int color = resources.getColor(R.color.colorOnSurface, context.getTheme());
        iconPickerDialog = new DrawablePickerDialog(context, title, ICON_RESOURCE_IDS, size, size, color);
        iconPickerDialog.addListener(this);

        final ImageView icon = root.findViewById(R.id.icon);
        icon.setOnClickListener(view -> {
            drawableStateController.stop();
            iconPickerDialog.show();
        });

        drawableStateController = new DrawableStateController(icon.getBackground());
        drawableStateController.pressAndRelease(SHOW_ICON_RIPPLE_AFTER_MILLISECONDS, HIDE_ICON_RIPPLE_AFTER_MILLISECONDS, true);
    }

    @Override
    public void onDestroyView() {
        iconPickerDialog.removeListener(this);
        drawableStateController.stop();
        super.onDestroyView();
    }

}
