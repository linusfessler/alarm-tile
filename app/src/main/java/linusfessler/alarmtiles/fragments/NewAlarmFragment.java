package linusfessler.alarmtiles.fragments;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.NewAlarmFragmentBinding;
import linusfessler.alarmtiles.model.AlarmTile;
import linusfessler.alarmtiles.viewmodel.NewAlarmViewModel;

public class NewAlarmFragment extends Fragment {

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

    private AlarmTile alarmTile;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = NewAlarmFragmentArgs.fromBundle(requireArguments()).getAlarmTile();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final NewAlarmViewModel viewModel = ViewModelProviders.of(this).get(NewAlarmViewModel.class);

        // FIXME: Should be able to rely on model not being null
        if (alarmTile.getBasicSettings() != null && alarmTile.getBasicSettings().getIconResourceId() != null) {
            viewModel.setName(alarmTile.getBasicSettings().getName());
            viewModel.setIconResourceId(alarmTile.getBasicSettings().getIconResourceId());
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

        // Since there is currently no app:error, we can not use data binding
        /*final TextInputLayout nameInputLayout = binding.getRoot().findViewById(R.id.name_input_layout);
        nameInputLayout.setError("Please enter a name");
        viewModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(final Observable sender, final int propertyId) {
                if (viewModel.isNameError()) {
                    nameInputLayout.setError("Please enter a name");
                }
            }
        });*/

        /*final RecyclerView iconRecyclerView = binding.getRoot().findViewById(R.id.icon_recycler_view);
        iconRecyclerView.setHasFixedSize(true);
        iconRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        iconRecyclerView.setAdapter(new DrawableResourceAdapter(ICON_RESOURCE_IDS));*/

        final FlexboxLayout icons = binding.getRoot().findViewById(R.id.icons);
        for (final int iconResourceId : ICON_RESOURCE_IDS) {
            final ImageView icon = (ImageView) inflater.inflate(R.layout.icon, icons, false);
            icon.setImageResource(iconResourceId);
            icons.addView(icon);
        }

        final MaterialButton button = binding.getRoot().findViewById(R.id.next_button);
        button.setOnClickListener(v -> {
            alarmTile.getBasicSettings().setName(viewModel.getName());
            alarmTile.getBasicSettings().setIconResourceId(viewModel.getIconResourceId());

            final NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(NewAlarmFragmentDirections.actionNewAlarmFragmentToFallingAsleepFragment(alarmTile));
        });

        return binding.getRoot();
    }

}
