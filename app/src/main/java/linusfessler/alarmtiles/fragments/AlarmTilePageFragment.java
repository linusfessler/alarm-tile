package linusfessler.alarmtiles.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.AlarmTilePageConfiguration;
import linusfessler.alarmtiles.AlarmTilePageListener;
import linusfessler.alarmtiles.AlarmTileRecyclerViewAdapter;
import linusfessler.alarmtiles.AppDatabase;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.SpanningGridLayoutManager;
import linusfessler.alarmtiles.model.AlarmTile;

public class AlarmTilePageFragment extends Fragment implements AlarmTilePageListener {

    private static final String PAGE_NUMBER_ARG_NAME = "page_number";

    private int pageNumber;

    private transient AppDatabase db;
    private transient NavController navController;
    private transient AlertDialog deleteDialog;

    public static AlarmTilePageFragment newInstance(final int pageNumber) {
        final AlarmTilePageFragment fragment = new AlarmTilePageFragment();

        final Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER_ARG_NAME, pageNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = requireArguments().getInt(PAGE_NUMBER_ARG_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final RecyclerView recyclerView = new RecyclerView(requireContext());

        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);

        final int orientation = getResources().getConfiguration().orientation;
        final AlarmTilePageConfiguration pageConfiguration = AlarmTilePageConfiguration.fromOrientation(orientation);

        final GridLayoutManager layoutManager = new SpanningGridLayoutManager(requireContext(), pageConfiguration.getRowCount(), pageConfiguration.getColumnCount());
        recyclerView.setLayoutManager(layoutManager);

        final AlarmTileRecyclerViewAdapter adapter = new AlarmTileRecyclerViewAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getInstance(requireContext());
        final LiveData<List<AlarmTile>> liveAlarmTiles = db.alarmTiles().selectAll();
        liveAlarmTiles.observeForever(alarmTiles -> {
            final List<AlarmTile> alarmTilesForPage = new ArrayList<>();

            final int startIndex = pageNumber * pageConfiguration.getCount();
            int endIndex = startIndex + pageConfiguration.getCount() - 1;
            endIndex = Math.min(endIndex, alarmTiles.size() - 1);

            for (int i = startIndex; i <= endIndex; i++) {
                alarmTilesForPage.add(alarmTiles.get(i));
            }

            adapter.setAlarmTiles(alarmTilesForPage);
        });

        navController = Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment));
        deleteDialog = new AlertDialog.Builder(requireContext())
                .setMessage(R.string.dialog_delete_message)
                .setNegativeButton(R.string.dialog_no, null)
                .create();

        return recyclerView;
    }

    @Override
    public void onAlarmTileClicked(final AlarmTile alarmTile) {
        final NavDirections direction = MainFragmentDirections.actionMainFragmentToSettingsContainerFragment(alarmTile);
        navController.navigate(direction);
    }

    @Override
    public void onAlarmTileLongClicked(final AlarmTile alarmTile) {
        final String dialogTitle = alarmTile.getGeneralSettings().getName();
        if (!dialogTitle.equals("")) {
            deleteDialog.setTitle(dialogTitle);
        }

        final String dialogYes = getString(R.string.dialog_yes);
        deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, dialogYes, (dialog, which) ->
                Executors.newSingleThreadExecutor().submit(() -> db.alarmTiles().delete(alarmTile)));

        deleteDialog.show();
    }

    @Override
    public void onDestroyView() {
        deleteDialog.dismiss();
        super.onDestroyView();
    }

}
