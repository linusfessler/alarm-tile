package linusfessler.alarmtiles.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import linusfessler.alarmtiles.DrawablePickerDialog;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentGeneralSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.GeneralSettingsViewModel;

public class GeneralSettingsFragment extends SettingsFragment implements DrawablePickerDialog.OnDrawablePickedListener {

    private static final String ALARM_TILE_ARG_NAME = "alarm_tile";

    private static final int[] ICON_RESOURCE_IDS = {
            R.drawable.ic_add_24px,
            R.drawable.ic_alarm_24px,
            R.drawable.ic_alarm_off_24px,
            R.drawable.ic_call_made_24px,
            R.drawable.ic_call_missed_outgoing_24px,
            R.drawable.ic_check_24px,
            R.drawable.ic_dashboard_24px,
            R.drawable.ic_delete_24px,
            R.drawable.ic_extension_24px,
            R.drawable.ic_format_list_bulleted_24px,
            R.drawable.ic_info_24px,
            R.drawable.ic_music_off_24px,
            R.drawable.ic_notifications_active_24px,
            R.drawable.ic_schedule_24px,
            R.drawable.ic_snooze_24px,
            R.drawable.ic_timer_24px,
            R.drawable.ic_timer_off_24px,
            R.drawable.ic_trending_down_24px,
            R.drawable.ic_trending_flat_24px,
            R.drawable.ic_trending_up_24px,
    };

    private AlarmTile alarmTile;
    private GeneralSettingsViewModel viewModel;
    private DrawablePickerDialog iconPickerDialog;

    public static GeneralSettingsFragment newInstance(final AlarmTile alarmTile) {
        final GeneralSettingsFragment fragment = new GeneralSettingsFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ALARM_TILE_ARG_NAME, alarmTile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.general_settings_title;
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

        viewModel = ViewModelProviders.of(this).get(GeneralSettingsViewModel.class);
        viewModel.setAlarmTile(alarmTile);
        final FragmentGeneralSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_general_settings, container, false);
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initIconPicker(view);
    }

    @Override
    public void onDrawablePicked(final int resourceId) {
        final String resourceName = getResources().getResourceEntryName(resourceId);
        viewModel.setIconResourceName(resourceName);
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
        icon.setOnClickListener(view -> iconPickerDialog.show());
    }

    @Override
    public void onDestroyView() {
        iconPickerDialog.removeListener(this);
        iconPickerDialog.dismiss();
        super.onDestroyView();
    }

}
