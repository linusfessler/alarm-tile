package linusfessler.alarmtiles;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import linusfessler.alarmtiles.fragments.MainFragment;
import linusfessler.alarmtiles.fragments.MainFragmentDirections;
import linusfessler.alarmtiles.model.AlarmTile;

public class AlarmTileRecyclerViewAdapter extends RecyclerView.Adapter<AlarmTileViewHolder> {

    private final Activity activity;
    private final MainFragment mainFragment;
    private final AppDatabase db;
    private final AlertDialog deleteDialog;

    private List<AlarmTile> alarmTiles = Collections.emptyList();

    public AlarmTileRecyclerViewAdapter(final Activity activity, final MainFragment mainFragment) {
        this.activity = activity;
        this.mainFragment = mainFragment;
        db = AppDatabase.getInstance(this.activity);

        deleteDialog = new AlertDialog.Builder(this.activity)
                .setMessage(R.string.dialog_delete_message)
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> {
                })
                .create();
    }

    @NonNull
    @Override
    public AlarmTileViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View alarmTileView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_alarm_tile, parent, false);
        return new AlarmTileViewHolder(alarmTileView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmTileViewHolder viewHolder, final int position) {
        final AlarmTile alarmTile = alarmTiles.get(position);

        viewHolder.setName(alarmTile.getGeneralSettings().getName());
        viewHolder.setIconResourceId(alarmTile.getGeneralSettings().getIconResourceId());

        //viewHolder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(MainFragmentDirections.actionMainFragmentToSettingsContainerFragment()));
        viewHolder.itemView.setOnClickListener(v -> {
            // TODO: Don't use view models for communication between fragments
            mainFragment.initViewModels(alarmTile);
            final NavController navController = Navigation.findNavController(activity.findViewById(R.id.nav_host_fragment));
            navController.navigate(MainFragmentDirections.actionMainFragmentToSettingsContainerFragment());
        });

        viewHolder.itemView.setOnLongClickListener(v -> {
            final String dialogTitle = alarmTile.getGeneralSettings().getName();
            deleteDialog.setTitle(dialogTitle);

            final String dialogYes = activity.getString(R.string.dialog_yes);
            deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, dialogYes, (dialog, which) ->
                    Executors.newSingleThreadExecutor().submit(() -> db.alarmTiles().delete(alarmTile)));

            deleteDialog.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return alarmTiles.size();
    }

    public void setAlarmTiles(final List<AlarmTile> alarmTiles) {
        this.alarmTiles = alarmTiles;
        notifyDataSetChanged();
    }

    public void dismissDeleteDialog() {
        deleteDialog.dismiss();
    }
}
