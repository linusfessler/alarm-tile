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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executors;

import linusfessler.alarmtiles.AlarmTileDao;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.databinding.FragmentSettingsContainerBinding;
import linusfessler.alarmtiles.model.AlarmTile;

public class SettingsContainerFragment extends Fragment {

    private AlarmTile alarmTile;
    private AppDatabase db;
    private FragmentManager fragmentManager;
    private NavController navController;

    private MaterialToolbar toolbar;
    private MaterialButton saveButton;
    private BottomNavigationView bottomNavigation;

    private AlertDialog backConfirmationDialog;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmTile = SettingsContainerFragmentArgs.fromBundle(requireArguments()).getAlarmTile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final FragmentSettingsContainerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_container, container, false);

        toolbar = binding.toolbar;
        saveButton = binding.saveButton;
        bottomNavigation = binding.bottomNavigation;

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(requireContext());
        fragmentManager = getChildFragmentManager();
        navController = Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment));

        initSaveButton();
        initBottomNavigation();
        initBackConfirmationDialog();
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

    private void initBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            final SettingsFragment fragment = getFragmentFromMenuItemId(item.getItemId());
            setChildFragment(fragment);
            return true;
        });
        setChildFragment(GeneralSettingsFragment.newInstance(alarmTile));
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

    private SettingsFragment getFragmentFromMenuItemId(final int index) {
        switch (index) {
            case R.id.menu_general:
                return GeneralSettingsFragment.newInstance(alarmTile);
            case R.id.menu_fall_asleep:
                return FallAsleepSettingsFragment.newInstance(alarmTile);
            case R.id.menu_sleep:
                return SleepSettingsFragment.newInstance(alarmTile);
            case R.id.menu_wake_up:
                return WakeUpSettingsFragment.newInstance(alarmTile);
            case R.id.menu_snooze:
                return SnoozeSettingsFragment.newInstance(alarmTile);
            default:
                throw new IllegalStateException("Bottom navigation bar has unknown menu items.");
        }
    }

    private void setChildFragment(final SettingsFragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        toolbar.setTitle(fragment.getTitleResourceId());
    }

    @Override
    public void onDestroyView() {
        backConfirmationDialog.dismiss();
        super.onDestroyView();
    }
}
