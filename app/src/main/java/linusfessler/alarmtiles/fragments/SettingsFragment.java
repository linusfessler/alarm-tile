package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executors;

import linusfessler.alarmtiles.AlarmTileDao;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSettingsBinding;
import linusfessler.alarmtiles.model.AlarmTile;

public class SettingsFragment extends Fragment {

    private AlarmTile alarmTile;
    private AppDatabase db;
    private NavController navController;

    private MaterialToolbar toolbar;
    private MaterialButton saveButton;

    private AlertDialog backConfirmationDialog;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = SettingsFragmentArgs.fromBundle(requireArguments()).getAlarmTile();

        if (savedInstanceState == null) {
            initChildFragments();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        toolbar = binding.toolbar;
        saveButton = binding.saveButton;

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());
        navController = Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment));

        initToolbar();
        initSaveButton();
        initBackConfirmationDialog();
    }

    private void initChildFragments() {
        final GeneralSettingsFragment generalSettingsFragment = GeneralSettingsFragment.newInstance(alarmTile);
        final FallAsleepSettingsFragment fallAsleepSettingsFragment = FallAsleepSettingsFragment.newInstance(alarmTile);
        final SleepSettingsFragment sleepSettingsFragment = SleepSettingsFragment.newInstance(alarmTile);
        final WakeUpSettingsFragment wakeUpSettingsFragment = WakeUpSettingsFragment.newInstance(alarmTile);

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_container, generalSettingsFragment)
                .add(R.id.fragment_container, fallAsleepSettingsFragment)
                .add(R.id.fragment_container, sleepSettingsFragment)
                .add(R.id.fragment_container, wakeUpSettingsFragment)
                .commit();
    }

    private void initToolbar() {
        final String title = getString(alarmTile.getId() == null ? R.string.alarm_tile_create : R.string.alarm_tile_edit);
        toolbar.setTitle(title);
    }

    private void initSaveButton() {
        saveButton.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().submit(() -> {
                final AlarmTileDao alarmTiles = db.alarmTiles();
                if (alarmTile.getId() == null) {
                    alarmTiles.insert(alarmTile);
                } else {
                    alarmTiles.update(alarmTile);
                }
            });

            navController.popBackStack();
        });
    }

    private void initBackConfirmationDialog() {
        backConfirmationDialog = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.dialog_back_title)
                .setMessage(R.string.dialog_back_message)
                .setPositiveButton(R.string.dialog_yes, (dialog, which) -> navController.popBackStack())
                .setNegativeButton(R.string.dialog_no, null)
                .create();

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backConfirmationDialog.show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        backConfirmationDialog.dismiss();
        super.onDestroyView();
    }

}
