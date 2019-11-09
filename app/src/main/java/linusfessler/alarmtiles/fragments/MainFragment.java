package linusfessler.alarmtiles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import linusfessler.alarmtiles.AlarmTilePageListener;
import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.model.AlarmTile;

public class MainFragment extends Fragment implements AlarmTilePageListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*final AppDatabase db = AppDatabase.getInstance(requireContext());
        final LiveData<List<AlarmTile>> liveAlarmTiles = db.alarmTiles().selectAll();
        liveAlarmTiles.observeForever(adapter::setAlarmTiles);*/
    }

    @Override
    public void onAlarmTileClicked(final AlarmTile alarmTile) {

    }

    @Override
    public void onAlarmTileLongClicked(final AlarmTile alarmTile) {

    }

}
